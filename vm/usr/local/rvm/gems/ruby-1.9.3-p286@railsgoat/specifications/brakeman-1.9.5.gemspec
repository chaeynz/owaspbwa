# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "brakeman"
  s.version = "1.9.5"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Justin Collins"]
  s.date = "2013-04-05"
  s.description = "Brakeman detects security vulnerabilities in Ruby on Rails applications via static analysis."
  s.executables = ["brakeman"]
  s.files = ["bin/brakeman"]
  s.homepage = "http://brakemanscanner.org"
  s.licenses = ["MIT"]
  s.require_paths = ["lib"]
  s.rubygems_version = "1.8.24"
  s.summary = "Security vulnerability scanner for Ruby on Rails."

  if s.respond_to? :specification_version then
    s.specification_version = 4

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<ruby_parser>, ["~> 3.1.1"])
      s.add_runtime_dependency(%q<ruby2ruby>, ["= 2.0.3"])
      s.add_runtime_dependency(%q<terminal-table>, ["~> 1.4"])
      s.add_runtime_dependency(%q<fastercsv>, ["~> 1.5"])
      s.add_runtime_dependency(%q<highline>, ["~> 1.6"])
      s.add_runtime_dependency(%q<erubis>, ["~> 2.6"])
      s.add_runtime_dependency(%q<haml>, ["< 5.0", ">= 3.0"])
      s.add_runtime_dependency(%q<sass>, ["~> 3.0"])
      s.add_runtime_dependency(%q<slim>, ["~> 1.3.6"])
      s.add_runtime_dependency(%q<multi_json>, ["~> 1.2"])
    else
      s.add_dependency(%q<ruby_parser>, ["~> 3.1.1"])
      s.add_dependency(%q<ruby2ruby>, ["= 2.0.3"])
      s.add_dependency(%q<terminal-table>, ["~> 1.4"])
      s.add_dependency(%q<fastercsv>, ["~> 1.5"])
      s.add_dependency(%q<highline>, ["~> 1.6"])
      s.add_dependency(%q<erubis>, ["~> 2.6"])
      s.add_dependency(%q<haml>, ["< 5.0", ">= 3.0"])
      s.add_dependency(%q<sass>, ["~> 3.0"])
      s.add_dependency(%q<slim>, ["~> 1.3.6"])
      s.add_dependency(%q<multi_json>, ["~> 1.2"])
    end
  else
    s.add_dependency(%q<ruby_parser>, ["~> 3.1.1"])
    s.add_dependency(%q<ruby2ruby>, ["= 2.0.3"])
    s.add_dependency(%q<terminal-table>, ["~> 1.4"])
    s.add_dependency(%q<fastercsv>, ["~> 1.5"])
    s.add_dependency(%q<highline>, ["~> 1.6"])
    s.add_dependency(%q<erubis>, ["~> 2.6"])
    s.add_dependency(%q<haml>, ["< 5.0", ">= 3.0"])
    s.add_dependency(%q<sass>, ["~> 3.0"])
    s.add_dependency(%q<slim>, ["~> 1.3.6"])
    s.add_dependency(%q<multi_json>, ["~> 1.2"])
  end
end
