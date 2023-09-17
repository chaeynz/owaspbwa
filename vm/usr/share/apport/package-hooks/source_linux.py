'''Apport package hook for the Linux kernel.

(c) 2008 Canonical Ltd.
Contributors:
Matt Zimmerman <mdz@canonical.com>
Martin Pitt <martin.pitt@canonical.com>

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2 of the License, or (at your
option) any later version.  See http://www.gnu.org/copyleft/gpl.html for
the full text of the license.
'''

import os
import subprocess
from apport.hookutils import *

SUBMIT_SCRIPT = "/usr/bin/kerneloops-submit"

def add_info(report, ui):

    # If running an upstream kernel, instruct reporter to file bug upstream
    abi = re.search("-(.*?)-", report['Uname'])
    if abi and (abi.group(1) == '999' or re.search("^0", abi.group(1))):
        ui.information("It appears you are currently running a mainline kernel.  It would be better to report this bug upstream at http://bugzilla.kernel.org/ so that the upstream kernel developers are aware of the issue.  If you'd still like to file a bug against the Ubuntu kernel, please boot with an official Ubuntu kernel and re-file.")
        report['UnreportableReason'] = 'The running kernel is not an Ubuntu kernel'
        return

    version_signature = report.get('ProcVersionSignature', '')
    if not version_signature.startswith('Ubuntu '):
        report['UnreportableReason'] = 'The running kernel is not an Ubuntu kernel'
        return

    # Prevent reports against linux-meta
    if report['SourcePackage'] == 'linux-meta':
        report['SourcePackage'] = 'linux'

    tags = []

    ui.information("As part of the bug reporting process, you'll be asked a series of questions to help provide a more descriptive bug report.  Please answer the following questions to the best of your ability.  Afterwards, a browser will be opened to finish filing this as a bug in the Launchpad bug tracking system.")

    # Try to tag bugs by category
    category_tags = ["audio",
                    "filesystem",
                    "graphics",
                    "kconfig",
                    "networking",
                    "hibernate resume",
                    "suspend resume",
                    "", ""]

    category = ui.choice("How would you categorize this issue?",
        ["Audio",
        "Filesystem",
        "Graphics",
        "Kernel Config",
        "Networking",
        "Hibernate/Resume",
        "Suspend/Resume",
        "Other",
        "I don't know"], False)

    if category == None: #user cancelled
        raise StopIteration

    tags.append(category_tags[category[0]])

    response = ui.yesno("A bug is considered a regression if the issue did not exist on a previous kernel.  Is this a regression?")
    if response == None: #user cancelled
        raise StopIteration

    report['Regression'] = "No"
    if response == True:
        report['Regression'] = "Yes"
        regression_tags = ["regression-release", "regression-potential",
            "regression-update", "regression-proposed",
            "regression-potential"]

        # add code to check if running dev release; if so, tag regression-potential and move on.
        regression = ui.choice("How would you describe the regression?",
            ["regression-release - A regression in a new stable release.",
            "regression-potential - A bug discovered in the development release that was not present in the stable release.",
            "regression-update - A regression introduced by an updated package in the stable release.",
            "regression-proposed - A regression introduced by a package in -proposed .",
            "I don't know."], False)

        #Don't Know response defaults to regression-potential
        tags.append(regression_tags[regression[0]])
        ui.information("After apport finishes collecting debug information, please note the most recent kernel version where this was not an issue when filling out the bug report.")

    response = ui.yesno("Can you recreate this bug with a specific series of steps?")

    if response == None: #user cancelled
        raise StopIteration

    if response == True:
        report['Reproducible'] = "Yes"
        ui.information("After apport finishes collecting debug information, please document your steps to reproduce the issue when filling out the bug report.")
    elif response == False:
        report['Reproducible'] = "No"
        frequency_options = ["Once a day.",
                    "Once every few days.",
                    "Once a week.",
                    "Once every few weeks.",
                    "Once a month.",
                    "Once every few months.",
                    "This has only happened once.",
                    "I don't know."]

        frequency = ui.choice("How often does this issue appear?",
                    frequency_options)
        report['Frequency'] = frequency_options[frequency[0]]

    tags.append("needs-upstream-testing")
    ui.information("Testing the upstream kernel can help isolate issues in Ubuntu kernel patches, discover a bug is fixed upstream, or confirm the issue exists upstream.  It would be great if you could test with the upstream kernel and let us know your results by posting a comment in the bug report.  For information on testing the upstream kernel, refer to https://wiki.ubuntu.com/KernelTeam/MainlineBuilds")

    report.setdefault('Tags', '')
    report['Tags'] += ' ' + ' '.join(tags)

    attach_hardware(report)
    attach_alsa(report)
    attach_wifi(report)

    staging_drivers = re.findall("(\w+): module is from the staging directory", report['BootDmesg'])
    staging_drivers.extend(re.findall("(\w+): module is from the staging directory", report['CurrentDmesg']))
    if staging_drivers:
        staging_drivers = list(set(staging_drivers))
        report['StagingDrivers'] = ' '.join(staging_drivers)
        report['Tags'] += ' staging'
        report['Title'] = '[STAGING] ' + report.get('Title', '')

    attach_file_if_exists(report, "/etc/initramfs-tools/conf.d/resume",
                      key="HibernationDevice")

    uname_release = os.uname()[2]
    lrm_package_name = 'linux-restricted-modules-%s' % uname_release
    lbm_package_name = 'linux-backports-modules-%s' % uname_release

    attach_related_packages(report, [lrm_package_name, lbm_package_name, 'linux-firmware'])

    if ('Failure' in report and report['Failure'] == 'oops'
            and 'OopsText' in report and os.path.exists(SUBMIT_SCRIPT)):
        #it's from kerneloops, ask the user whether to submit there as well
        if ui is not None:
            summary = report['OopsText']
            # Some OopsText begin with "--- [ cut here ] ---", so remove it
            summary = re.sub("---.*\n","",summary)
            first_line = re.match(".*\n", summary)
            ip = re.search("IP\:.*\n", summary)
            call_trace = re.search("Call Trace(.*\n){,10}",summary)
            oops = ''
            if first_line:
                oops += first_line.group(0)
            if ip:
                oops += ip.group(0)
            if call_trace:
                oops += call_trace.group(0)
            if ui.yesno("This report may also be submitted to "
                "http://kerneloops.org/ in order to help collect aggregate "
                "information about kernel problems. This aids in identifying "
                "widespread issues and problematic areas. A condensed "
                "summary of the Oops is shown below.  Would you like to submit "
                "information about this crash to kerneloops.org ?"
                "\n\n%s" % oops):
                text = report['OopsText']
                proc = subprocess.Popen(SUBMIT_SCRIPT,
                    stdin=subprocess.PIPE)
                proc.communicate(text)

