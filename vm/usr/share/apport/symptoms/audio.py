# Sound/audio related problem troubleshooter/triager
# Written by David Henningsson 2010, diwic@ubuntu.com
# License: BSD (see /usr/share/common-licenses/BSD )

description = 'Sound/audio related problems'

import apport
import re
import os
import subprocess

def run(report, ui):

    # is pulseaudio installed and running?
    package = check_pulseaudio_running(report, ui)
    if package is not None:
        return package

    # is JACK running?
    package = check_jackd_running(report, ui)
    if package is not None:
        return package

    # what problem does the user observe?
    problem = ask_problem(report, ui)

    # soundcard query
    package, card = soundcard_query(report, ui)
    if package is not None:
        return package

    # check that the default pulseaudio profile is correct
    package, channelcount = check_pulseaudio_profile(report, ui, card, problem)
    if package is not None:
        return package

    # playback problems
    if problem == 0 or problem == 1:
        check_volumes(report, ui, card, problem)
        # speaker test to see if it's pulseaudio or alsa.
        package = speaker_test(report, ui, card, channelcount)
        if package is not None:
            return package


    #TODO: Recording problems

    # hopefully, we should never come here
    s = ['Playback', 'Surround', 'Recording']
    return set_title(report, 'alsa-base', s[problem] + ' problem', card)


def ask_problem(report, ui):
    ''' returns problem code: 
      0 => Stereo playback
      1 => Surround playback
      2 => Recording
      or raises StopIteration '''
    problem = ui.choice('What particular problem do you observe?',
        ['Playback does not work, or is crackling', 
         'Surround playback problem (but stereo playback works)',
         'Recording does not work properly',
         'Sound problem with one or a few applications only'
        ])

    if problem is None:
        raise StopIteration
    problem = problem[0]

    if problem == 3:
        ui.information('Please use the "report bug" feature, available in '
         'the menu of the application,\nor use "ubuntu-bug <packagename>" '
         'to report a bug against the particular package.')
        raise StopIteration

    if not ui.yesno('Have you checked that your sound system is ' 
     'plugged in, and turned on?'):
        raise StopIteration
    return problem


def check_pulseaudio_running(report, ui):
    ''' Possible outcomes: None if pulseaudio is running, 
        pulseaudio if installed but not running, or
        StopIteration if pulseaudio is not installed. '''
    if subprocess.call(['pgrep', '-u', str(os.getuid()), '-x', 'pulseaudio']) == 0:
        return None 
    if subprocess.call(['pgrep', '-u', 'pulse', '-x', 'pulseaudio']) == 0:
        ui.information('PulseAudio is running as a system-wide daemon.\n'
         'This mode of operation is not supported by Ubuntu.\n'
         'If this is not intentional, ask for help on answers.launchpad.net.')
        raise StopIteration
    try:
        if apport.packaging.get_version('pulseaudio'):
            if ui.yesno('PulseAudio seems to have crashed. '
             'Do you wish to report a bug?'):
                return set_title(report, 'pulseaudio', 'Pulseaudio is not running')
            raise StopIteration
    except ValueError:
        pass

    ui.information('This troubleshooter currently '
     'only works for the Ubuntu flavors that use PulseAudio.\n'
     'Sorry for the inconvenience.')
    raise StopIteration

def check_jackd_running(report, ui):
    ''' Possible outcomes: None (jackd is not running),
        StopIteration (jackd is running). '''
    if subprocess.call(['pgrep', '-x', 'jackd']) == 0:
        ui.information('The JACK daemon is running.\n'
         'Using JACK alongside PulseAudio currently is not supported.\n'
         'If this is not intentional, ask for help on answers.launchpad.net.')
        raise StopIteration
    else:
        return None 

def pulseaudio_to_alsa_card(report, ui, pacardname):
    ''' Returns alsa card index '''

    pactlvalues = run_subprocess(report, 'PactlList',  ['pactl', 'list'])
    right_card = 0
    for line in pactlvalues.splitlines():
        if re.match('^\w+ #\d+', line):
            right_card = 0
            if ('Sink' in line) or ('Source' in line):
                right_card = 1
        if pacardname in line and right_card == 1: 
            right_card = 2
        if right_card != 2:
            continue
        s = re.search('alsa\.card = \"(.+)\"', line)
        if s is not None:
            return int(s.groups(1)[0])
    report['PactlListCardNotFound'] = pacardname
    report['PactlList'] = pactlvalues
    return -1
        

def check_pulseaudio_profile(report, ui, card, problem):
    ''' Returns package, channelmap '''
    # run pactl stat
    pactlvalues = run_subprocess(report, 'PactlStat', ['pactl', 'stat'])

    # parse pa card name and channel map
    pacardname = None
    if problem == 2:
        ss = "Default Source: alsa_input." 
    else:
        ss = "Default Sink: alsa_output."
    for line in pactlvalues.splitlines():
        if line.startswith(ss): 
            dummy1, dummy2, pacardname = line.partition(".")
            dummy1, dummy2, pacardnamefull = line.partition(": ")

    if pacardname is None:
        # something is seriously wrong with pulseaudio if we come here
        report['PactlStat'] = pactlvalues
        set_title(report, 'pulseaudio', 'pactl stat failed to find default card', card)
        return 'pulseaudio', None

    pacardname, dummy2, paprofile = pacardname.rpartition(".")

    # verify user has selected the correct fallback device
    # TODO: pacardname is not in the same format as the alsa card...
    if pulseaudio_to_alsa_card(report, ui, pacardnamefull) != int(card[0]):
        if not ui.yesno('You seem to have configured PulseAudio to use '
         'the "%s" card, while you want output from "%s".\n Please correct '
         'that using pavucontrol or the GNOME volume control. ' 
         'Continue anyway?' % (pacardname, card[2])):
            raise StopIteration

    if problem == 2:
        return None, None

    # check that the profile is correct
    if problem == 0 and (not "stereo" in paprofile):
        if not ui.yesno('You seem to have configured PulseAudio for '
         'surround output (%s).\n Please correct that using pavucontrol '
         'or the GNOME volume control. Continue anyway?' % paprofile):
            raise StopIteration
    if problem == 1 and (not "surround" in paprofile):
        if not ui.yesno("You don't seem to have configured PulseAudio "
         'for surround output (%s).\n Please correct that using pavucontrol '
         'or the GNOME volume control. Continue anyway?' % paprofile):
            raise StopIteration

    if problem == 0:
        return None, 2
    
    # Figure out channel count for speaker test later
    # e g surround-51 => 51 => 5+1 => 6
    dummy1, dummy2, cc = paprofile.rpartition("-")
    cc = int(cc)/10 + int(cc) % 10
    return None, cc

 
def soundcard_query(report, ui):
    ''' Returns a tuple of (package, card)
        card is [index, name, long-name]
        If package is not None, no need to examine further. '''

    # Parse /proc/asound/cards
    alsacards = []
    choices = []
    for card in open('/proc/asound/cards'):
        m = re.search(' (\d+) \[(\w+)\s*\]: (.+)', card)
        if not m is None:
            alsacards.append(m.groups(1))
            choices.append(m.groups(1)[2])

    # query user
    choices = choices + ['Another card not listed above', "I don't know"]
    cardchoice = ui.choice('What is the name of the sound card you are having problems with?', choices)
    if cardchoice is None:
        raise StopIteration
    cardchoice = cardchoice[0]
    
    # if the user does not know, just assume the first sound card...
    if cardchoice == len(alsacards)+1:
        cardchoice = 0 
 
    # if the card was not detected...
    if cardchoice == len(alsacards):
        if ui.yesno('Is your soundcard an external firewire card?'):
            # TODO: better ffado troubleshooting as well...
            if ui.yesno('External firewire cards require manual setup.\n'
             'Documentation is here: https://help.ubuntu.com/community/HowToJACKConfiguration\n'
             'Would you like to continue?'):
                return 'libffado1', None
            raise StopIteration
        # soundcard not detected
        set_title(report, 'alsa-base', 'Soundcard not detected')
        return 'alsa-base', None

    report['SelectedCard'] = ' '.join(alsacards[cardchoice])
    return None, alsacards[cardchoice]


def check_volumes(report, ui, card, problem):
    ''' check for mutes '''
    mixervalues = run_subprocess(report, 'amixer', ['amixer', '-c', str(card[0])]) 
    checkthisone = 0
    levels = ""
    mixervalues = mixervalues.splitlines()
    for m in mixervalues:
        s = re.search("\w[^']*'(.+)'", m)
        if not s is None:
            currentcontrol = s.groups(1)[0]
            checkthisone = 0
            if currentcontrol in ['Master', 'PCM', 'Wave', 'Front', 
             'Master Front', 'Analog Wave', 'Surround', 'Headphone', 'Speaker']:
                checkthisone = 1
            if problem == 1 and currentcontrol in ['Center', 'LFE', 'Side']:
                checkthisone = 1
        elif checkthisone != 1:
            continue
        if "[off]" in m:
            levels += currentcontrol + ' is muted\n'
        s = re.search("\[(\d+)%\]", m)
        if s is None:
            continue
        level = int(s.groups(1)[0])
        if level >= 70:
            continue
        levels += currentcontrol + ' is at {0}%\n'.format(level)

    if levels != "":
        if not ui.yesno('The following mixer control(s) are set quite low: \n' + levels +
         'Please try to set them higher (e g by running "alsamixer -c' +
         str(card[0]) + '" in a terminal) and see if that solves the problem.\n' 
         'Would you like to continue troubleshooting anyway?\n'):
            raise StopIteration


def speaker_test(report, ui, card, channelcount):
    ''' Returns package if it successfully finds one. '''

    ui.information('Next, a speaker test will be performed. For your safety,\n'
     'if you have headphones on, take them off to avoid damaging your ears.\n'
     'Press OK to hear the test tone. It should alternate between %d channels.'
     % channelcount)
    run_subprocess(report, 'SpeakerTestAlsa', ['pasuspender', '--', 
        'speaker-test', '-l', '3', '-c', str(channelcount), '-b', '100000', 
        '-D', 'plughw:' + str(card[0]), '-t', 'sine']) 
    if not ui.yesno('Were the test tones played back correctly?'):
        return set_title(report, 'alsa-base', 'ALSA test tone not correctly played back', card)

    ui.information('Press OK to hear the second test tone. It should alternate between channels.')
    run_subprocess(report, 'SpeakerTestPulse', ['speaker-test', '-l', '3', 
     '-c', str(channelcount), '-b', '100000', '-D', 'pulse', '-t', 'sine'])
    if not ui.yesno('Were the test tones played back correctly?'):
        return set_title(report, 'pulseaudio', 'PA test tone failed (alsa tone succeeded)', card)

    return None

def set_title(report, return_value, title, card=None):
    if card is not None:
        try:
            filename = "/proc/asound/card%s/codec#0" % card[0]
            f = open(filename, "r")
            dummy1, dummy2, codecname = f.readline().partition(":")
            f.close()
            title = '[' + codecname.strip() +'] '+title
        except:
            title = '[' + card[2] + '] '+title
        
    report['Title'] = title
    return return_value

def run_subprocess(report, reportkey, args):
    ''' Helper function to run a subprocess.  
        Returns stdout and writes stderr, if any, to the report. '''

    # avoid localized strings
    env = os.environ
    env['LC_MESSAGES'] = 'C'
    sub_mon = subprocess.Popen(args,
        stdout=subprocess.PIPE, stderr=subprocess.PIPE, env=env)
    sub_out, sub_err = sub_mon.communicate()
    if sub_err is not None and (len(str(sub_err).strip()) > 0):
        report[reportkey+'Stderr'] = ' '.join(sub_err)
    return sub_out
    
