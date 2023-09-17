require 'breakpoint'
require 'optparse'
require 'timeout'
require 'tmpdir'

# Uses the following standard options overwritten by options that might
# have been previously defined.
Options = {} unless defined?(Options)
Options.replace({
  :ClientURI  => nil,
  :ServerURI  => "druby://localhost:42531",
  :RetryDelay => 3,
  :Permanent  => false,
  :Verbose    => false
}.merge(Options))

ARGV.options do |opts|
  script_name = File.basename($0)
  opts.banner = [
    "Usage: ruby #{script_name} [Options] [server uri]",
    "",
    "This tool lets you connect to a breakpoint service ",
    "which was started via Breakpoint.activate_drb.",
    "",
    "The server uri defaults to druby://localhost:42531",
    "",
    "Having trouble or need help?",
    "* Homepage:  http://ruby-breakpoint.rubyforge.org/ (has FAQ!)",
    "* Author:    Florian Gross, flgr@ccan.de (Read homepage first!)"
  ].join("\n")

  opts.separator ""

  opts.on("-c", "--client-uri=uri",
    "Run the client on the specified uri.",
    "This can be used to specify the port",
    "that the client uses to allow for back",
    "connections from the server.",
    "Default: Find a good URI automatically.",
    "Example: -c druby://localhost:12345"
  ) { |Options[:ClientURI]| }

  opts.on("-s", "--server-uri=uri",
    "Connect to the server specified at the",
    "specified uri.",
    "Default: druby://localhost:42531"
  ) { |Options[:ServerURI]| }

  opts.on("-R", "--retry-delay=delay", Integer,
    "Automatically try to reconnect to the",
    "server after delay seconds when the",
    "connection failed or timed out.",
    "A value of 0 disables automatical",
    "reconnecting completely.",
    "Default: 10"
  ) { |Options[:RetryDelay]| }

  opts.on("-P", "--[no-]permanent",
    "Run the breakpoint client in permanent mode.",
    "This means that the client will keep continue",
    "running even after the server has closed the",
    "connection. Useful for example in Rails.",
    "Default: non-permanent"
  ) { |Options[:Permanent]| }

  opts.on("-V", "--[no-]verbose",
    "Run the breakpoint client in verbose mode.",
    "Will produce more messages, for example between",
    "individual breakpoints. This might help in seeing",
    "that the breakpoint client is still alive, but adds",
    "quite a bit of clutter.",
    "Default: non-verbose"
  ) { |Options[:Verbose]| }

  opts.separator ""

  opts.on("-h", "--help",
    "Show this help message."
  ) { puts opts; exit }
  opts.on("-v", "--version",
    "Display the version information."
  ) do
    id = %q$Id: breakpoint_client 80 2005-07-28 18:15:20Z flgr $
    puts id.sub("Id: ", "")
    puts "(Breakpoint::Version = #{Breakpoint::Version})"
    exit
  end

  opts.parse!
end

Options[:ServerURI] = ARGV[0] if ARGV[0]
Options[:ClientURI] ||= case Options[:ServerURI]
  when /^drbunix:(.+)$/i then
    "drbunix:" << File.join(Dir.tmpdir, $1.gsub(/\W/, "_")) << ".breakpoint_client"
  when %r{^druby://(localhost|127\.0\.0\.1|::1):(\d+)$}i then
    "druby://" << $1 << ":" << $2.succ
end
puts "ClientURI is #{Options[:ClientURI] || "unspecified"}" if Options[:Verbose]

module Handlers
  extend self

  def breakpoint_handler(workspace, message)
    puts message
    IRB.start(nil, nil, workspace)

    puts ""
    if Options[:Verbose] then
      puts "Resumed execution. Waiting for next breakpoint...", ""
    end
  end

  def eval_handler(code)
    result = eval(code, TOPLEVEL_BINDING)
    if result then
      DRbObject.new(result)
    else
      result
    end
  end

  def collision_handler()
    msg = [
      "  *** Breakpoint service collision ***",
      "  Another Breakpoint service tried to use the",
      "  port already occupied by this one. It will",
      "  keep waiting until this Breakpoint service",
      "  is shut down.",
      "  ",
      "  If you are using the Breakpoint library for",
      "  debugging a Rails or other CGI application",
      "  this likely means that this Breakpoint",
      "  session belongs to an earlier, outdated",
      "  request and should be shut down via 'exit'."
    ].join("\n")

    if RUBY_PLATFORM["win"] then
      # This sucks. Sorry, I'm not doing this because
      # I like funky message boxes -- I need to do this
      # because on Windows I have no way of displaying
      # my notification via puts() when gets() is still
      # being performed on STDIN. I have not found a
      # better solution.
      begin
        require 'tk'
        root = TkRoot.new { withdraw }
        Tk.messageBox('message' => msg, 'type' => 'ok')
        root.destroy
      rescue Exception
        puts "", msg, ""
      end
    else
      puts "", msg, ""
    end
  end
end

# Used for checking whether we are currently in the reconnecting loop.
reconnecting = false

loop do
  DRb.start_service(Options[:ClientURI])

  begin
    service = DRbObject.new(nil, Options[:ServerURI])

    begin
      ehandler = Handlers.method(:eval_handler)
      chandler = Handlers.method(:collision_handler)
      handler = Handlers.method(:breakpoint_handler)
      service.eval_handler = ehandler
      service.collision_handler = chandler
      service.handler = handler

      reconnecting = false
      if Options[:Verbose] then
        puts "Connection established. Waiting for breakpoint...", ""
      end

      loop do
        begin
          service.ping
        rescue DRb::DRbConnError => error
          puts "Server exited. Closing connection...", ""
          DRb.stop_service
          exit! unless Options[:Permanent]
          break
        end

        sleep(0.5)
      end
    ensure
      service.eval_handler = nil
      service.collision_handler = nil
      service.handler = nil
    end
  rescue Exception => error
    if Options[:RetryDelay] > 0 then
      if not reconnecting then
        reconnecting = true
        puts "No connection to breakpoint service at #{Options[:ServerURI]} " +
           "(#{error.class})"
        puts error.backtrace if $DEBUG
        puts "Tries to connect will be made every #{Options[:RetryDelay]} seconds..."
      end

      sleep Options[:RetryDelay]
      retry
    else
      raise
    end
  end
end
