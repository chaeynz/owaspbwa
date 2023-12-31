��    '      T  5   �      `  >  a     �     �     �     �     �     �     �                4     A     O     d     x     �     �     �     �     �     �               ,     @     M  4   j     �     �     �     �     �               1  
   =     H     Y  �  i  >       W"     k"     z"     �"     �"     �"     �"     �"     �"     �"     #     #     3#     I#     _#     u#     �#     �#     �#     �#     �#     �#     $     %$     4$  7   Q$     �$     �$     �$     �$     �$     �$     %     %  
   '%     2%     C%                   	                         !          %                                    $   
                #                       &                      "                   '    %s [options]

COMMANDS:
  -h, --help                            Show this help
      --version                         Show version
      --dump-conf                       Dump default configuration
      --dump-modules                    Dump list of available modules
      --dump-resample-methods           Dump available resample methods
      --cleanup-shm                     Cleanup stale shared memory segments
      --start                           Start the daemon if it is not running
  -k  --kill                            Kill a running daemon
      --check                           Check for a running daemon (only returns exit code)

OPTIONS:
      --system[=BOOL]                   Run as system-wide instance
  -D, --daemonize[=BOOL]                Daemonize after startup
      --fail[=BOOL]                     Quit when startup fails
      --high-priority[=BOOL]            Try to set high nice level
                                        (only available as root, when SUID or
                                        with elevated RLIMIT_NICE)
      --realtime[=BOOL]                 Try to enable realtime scheduling
                                        (only available as root, when SUID or
                                        with elevated RLIMIT_RTPRIO)
      --disallow-module-loading[=BOOL]  Disallow module user requested module
                                        loading/unloading after startup
      --disallow-exit[=BOOL]            Disallow user requested exit
      --exit-idle-time=SECS             Terminate the daemon when idle and this
                                        time passed
      --module-idle-time=SECS           Unload autoloaded modules when idle and
                                        this time passed
      --scache-idle-time=SECS           Unload autoloaded samples when idle and
                                        this time passed
      --log-level[=LEVEL]               Increase or set verbosity level
  -v                                    Increase the verbosity level
      --log-target={auto,syslog,stderr} Specify the log target
      --log-meta[=BOOL]                 Include code location in log messages
      --log-time[=BOOL]                 Include timestamps in log messages
      --log-backtrace=FRAMES            Include a backtrace in log messages
  -p, --dl-search-path=PATH             Set the search path for dynamic shared
                                        objects (plugins)
      --resample-method=METHOD          Use the specified resampling method
                                        (See --dump-resample-methods for
                                        possible values)
      --use-pid-file[=BOOL]             Create a PID file
      --no-cpu-limit[=BOOL]             Do not install CPU load limiter on
                                        platforms that support it.
      --disable-shm[=BOOL]              Disable shared memory support.

STARTUP SCRIPT:
  -L, --load="MODULE ARGUMENTS"         Load the specified plugin module with
                                        the specified argument
  -F, --file=FILENAME                   Run the specified script
  -C                                    Open a command line on the running TTY
                                        after startup

  -n                                    Don't load default script file
 Analog Headphones Analog Input Analog Line-In Analog Microphone Analog Mono Analog Mono Duplex Analog Mono Output Analog Output Analog Output (LFE) Analog Radio Analog Stereo Analog Stereo Duplex Analog Surround 2.1 Analog Surround 3.0 Analog Surround 3.1 Analog Surround 4.0 Analog Surround 4.1 Analog Surround 5.0 Analog Surround 5.1 Analog Surround 6.0 Analog Surround 6.1 Analog Surround 7.0 Analog Surround 7.1 Analog Video Failed to initialize daemon. Fresh high-resolution timers available! Bon appetit! Front Center Front Left-of-center Front Right-of-center Module initalization failed No authorization key Optimized build: no Optimized build: yes Rear Center Top Center Top Front Center Top Rear Center Project-Id-Version: pulseaudio
Report-Msgid-Bugs-To: FULL NAME <EMAIL@ADDRESS>
POT-Creation-Date: 2009-10-26 09:54+0000
PO-Revision-Date: 2010-02-07 11:54+0000
Last-Translator: Robert Readman <Unknown>
Language-Team: English (United Kingdom) <en_GB@li.org>
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-Launchpad-Export-Date: 2010-07-14 12:19+0000
X-Generator: Launchpad (build 9518)
 %s [options]

COMMANDS:
  -h, --help                            Show this help
      --version                         Show version
      --dump-conf                       Dump default configuration
      --dump-modules                    Dump list of available modules
      --dump-resample-methods           Dump available resample methods
      --cleanup-shm                     Cleanup stale shared memory segments
      --start                           Start the daemon if it is not running
  -k  --kill                            Kill a running daemon
      --check                           Check for a running daemon (only returns exit code)

OPTIONS:
      --system[=BOOL]                   Run as system-wide instance
  -D, --daemonize[=BOOL]                Daemonise after startup
      --fail[=BOOL]                     Quit when startup fails
      --high-priority[=BOOL]            Try to set high nice level
                                        (only available as root, when SUID or
                                        with elevated RLIMIT_NICE)
      --realtime[=BOOL]                 Try to enable realtime scheduling
                                        (only available as root, when SUID or
                                        with elevated RLIMIT_RTPRIO)
      --disallow-module-loading[=BOOL]  Disallow module user requested module
                                        loading/unloading after startup
      --disallow-exit[=BOOL]            Disallow user requested exit
      --exit-idle-time=SECS             Terminate the daemon when idle and this
                                        time passed
      --module-idle-time=SECS           Unload autoloaded modules when idle and
                                        this time passed
      --scache-idle-time=SECS           Unload autoloaded samples when idle and
                                        this time passed
      --log-level[=LEVEL]               Increase or set verbosity level
  -v                                    Increase the verbosity level
      --log-target={auto,syslog,stderr} Specify the log target
      --log-meta[=BOOL]                 Include code location in log messages
      --log-time[=BOOL]                 Include timestamps in log messages
      --log-backtrace=FRAMES            Include a backtrace in log messages
  -p, --dl-search-path=PATH             Set the search path for dynamic shared
                                        objects (plugins)
      --resample-method=METHOD          Use the specified resampling method
                                        (See --dump-resample-methods for
                                        possible values)
      --use-pid-file[=BOOL]             Create a PID file
      --no-cpu-limit[=BOOL]             Do not install CPU load limiter on
                                        platforms that support it.
      --disable-shm[=BOOL]              Disable shared memory support.

STARTUP SCRIPT:
  -L, --load="MODULE ARGUMENTS"         Load the specified plugin module with
                                        the specified argument
  -F, --file=FILENAME                   Run the specified script
  -C                                    Open a command line on the running TTY
                                        after startup

  -n                                    Don't load default script file
 Analogue Headphones Analogue Input Analogue Line-In Analogue Microphone Analogue Mono Analogue Mono Duplex Analogue Mono Output Analogue Output Analogue Output (LFE) Analogue Radio Analogue Stereo Analogue Stereo Duplex Analogue Surround 2.1 Analogue Surround 3.0 Analogue Surround 3.1 Analogue Surround 4.0 Analogue Surround 4.1 Analogue Surround 5.0 Analogue Surround 5.1 Analogue Surround 6.0 Analogue Surround 6.1 Analogue Surround 7.0 Analogue Surround 7.1 Analogue Video Failed to initialise daemon. Fresh high-resolution timers available! Enjoy ol' chap! Front Centre Front Left-of-centre Front Right-of-centre Module initalisation failed No authorisation key Optimised build: no Optimised build: yes Rear Centre Top Centre Top Front Centre Top Rear Centre 