# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "mailcatcher"
  s.version = "0.5.12"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Samuel Cochran"]
  s.cert_chain = ["-----BEGIN CERTIFICATE-----\nMIIDXDCCAkSgAwIBAgIBATANBgkqhkiG9w0BAQUFADA6MQ0wCwYDVQQDDARzajI2\nMRQwEgYKCZImiZPyLGQBGRYEc2oyNjETMBEGCgmSJomT8ixkARkWA2NvbTAeFw0x\nMzAzMTEyMzIyMThaFw0xNDAzMTEyMzIyMThaMDoxDTALBgNVBAMMBHNqMjYxFDAS\nBgoJkiaJk/IsZAEZFgRzajI2MRMwEQYKCZImiZPyLGQBGRYDY29tMIIBIjANBgkq\nhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsr60Eo/ttCk8GMTMFiPr3GoYMIMFvLak\nxSmTk9YGCB6UiEePB4THSSA5w6IPyeaCF/nWkDp3/BAam0eZMWG1IzYQB23TqIM0\n1xzcNRvFsn0aQoQ00k+sj+G83j3T5OOV5OZIlu8xAChMkQmiPd1NXc6uFv+Iacz7\nkj+CMsI9YUFdNoU09QY0b+u+Rb6wDYdpyvN60YC30h0h1MeYbvYZJx/iZK4XY5zu\n4O/FL2ChjL2CPCpLZW55ShYyrzphWJwLOJe+FJ/ZBl6YXwrzQM9HKnt4titSNvyU\nKzE3L63A3PZvExzLrN9u09kuWLLJfXB2sGOlw3n9t72rJiuBr3/OQQIDAQABo20w\nazAJBgNVHRMEAjAAMAsGA1UdDwQEAwIEsDAdBgNVHQ4EFgQU99dfRjEKFyczTeIz\nm3ZsDWrNC80wGAYDVR0RBBEwD4ENc2oyNkBzajI2LmNvbTAYBgNVHRIEETAPgQ1z\najI2QHNqMjYuY29tMA0GCSqGSIb3DQEBBQUAA4IBAQArNvPoDTdElU79folErf7u\nqFL/ZzuC53hyNG0bQGggKOZJ4PuzqI9AFGX6Viw+0iltZzuqo5WTuuOQknO1aJZl\nhp3Jijjd/vpxFuwalQT6JcN9V0iz81KiI+9KPRDxedX4zK9PD4Be+ZJgZBOalk6U\ngQM0w4nYX8KATozCgvkFvKOQrNBEa+BqPundb8r047VRuK9iPGaJJeBpBgXgCNQc\niAw+LFjcoeiI5naoCBHZ4uHGY0lXrxxcep3F/GFvKKYGrrI9c2j5awd0DaEzrCel\ngtfuNEm5Q0pV0/XpT5PsXz/rGnsR4CS7BaIcx+bAwl5pRopCzCHfl5Igay1vefPZ\n-----END CERTIFICATE-----\n"]
  s.date = "2013-05-30"
  s.description = "    MailCatcher runs a super simple SMTP server which catches any\n    message sent to it to display in a web interface. Run\n    mailcatcher, set your favourite app to deliver to\n    smtp://127.0.0.1:1025 instead of your default SMTP server,\n    then check out http://127.0.0.1:1080 to see the mail.\n"
  s.email = "sj26@sj26.com"
  s.executables = ["mailcatcher", "catchmail"]
  s.extra_rdoc_files = ["README.md", "LICENSE"]
  s.files = ["bin/mailcatcher", "bin/catchmail", "README.md", "LICENSE"]
  s.homepage = "http://mailcatcher.me"
  s.licenses = ["MIT"]
  s.require_paths = ["lib"]
  s.required_ruby_version = Gem::Requirement.new(">= 1.8.7")
  s.rubygems_version = "1.8.28"
  s.summary = "Runs an SMTP server, catches and displays email in a web interface."

  if s.respond_to? :specification_version then
    s.specification_version = 4

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<activesupport>, ["~> 3.0"])
      s.add_runtime_dependency(%q<eventmachine>, ["~> 1.0.0"])
      s.add_runtime_dependency(%q<haml>, ["< 5", ">= 3.1"])
      s.add_runtime_dependency(%q<mail>, ["~> 2.3"])
      s.add_runtime_dependency(%q<sinatra>, ["~> 1.2"])
      s.add_runtime_dependency(%q<sqlite3>, ["~> 1.3"])
      s.add_runtime_dependency(%q<thin>, ["~> 1.5.0"])
      s.add_runtime_dependency(%q<skinny>, ["~> 0.2.3"])
      s.add_development_dependency(%q<coffee-script>, [">= 0"])
      s.add_development_dependency(%q<compass>, [">= 0"])
      s.add_development_dependency(%q<rake>, [">= 0"])
      s.add_development_dependency(%q<rdoc>, [">= 0"])
      s.add_development_dependency(%q<sass>, [">= 0"])
    else
      s.add_dependency(%q<activesupport>, ["~> 3.0"])
      s.add_dependency(%q<eventmachine>, ["~> 1.0.0"])
      s.add_dependency(%q<haml>, ["< 5", ">= 3.1"])
      s.add_dependency(%q<mail>, ["~> 2.3"])
      s.add_dependency(%q<sinatra>, ["~> 1.2"])
      s.add_dependency(%q<sqlite3>, ["~> 1.3"])
      s.add_dependency(%q<thin>, ["~> 1.5.0"])
      s.add_dependency(%q<skinny>, ["~> 0.2.3"])
      s.add_dependency(%q<coffee-script>, [">= 0"])
      s.add_dependency(%q<compass>, [">= 0"])
      s.add_dependency(%q<rake>, [">= 0"])
      s.add_dependency(%q<rdoc>, [">= 0"])
      s.add_dependency(%q<sass>, [">= 0"])
    end
  else
    s.add_dependency(%q<activesupport>, ["~> 3.0"])
    s.add_dependency(%q<eventmachine>, ["~> 1.0.0"])
    s.add_dependency(%q<haml>, ["< 5", ">= 3.1"])
    s.add_dependency(%q<mail>, ["~> 2.3"])
    s.add_dependency(%q<sinatra>, ["~> 1.2"])
    s.add_dependency(%q<sqlite3>, ["~> 1.3"])
    s.add_dependency(%q<thin>, ["~> 1.5.0"])
    s.add_dependency(%q<skinny>, ["~> 0.2.3"])
    s.add_dependency(%q<coffee-script>, [">= 0"])
    s.add_dependency(%q<compass>, [">= 0"])
    s.add_dependency(%q<rake>, [">= 0"])
    s.add_dependency(%q<rdoc>, [">= 0"])
    s.add_dependency(%q<sass>, [">= 0"])
  end
end
