# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "gauntlt"
  s.version = "1.0.8"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["James Wickett", "Mani Tadayon"]
  s.date = "2014-03-06"
  s.description = "Using standard Gherkin language to define security tests, gauntlt happily wraps cucumber functionality and provides a security testing framework that security engineers, developers and operations teams can collaborate on together."
  s.email = ["james@gauntlt.org"]
  s.executables = ["gauntlt"]
  s.files = ["bin/gauntlt"]
  s.homepage = "https://github.com/gauntlt/gauntlt"
  s.licenses = ["MIT"]
  s.require_paths = ["lib"]
  s.rubygems_version = "1.8.28"
  s.summary = "behaviour-driven security using cucumber"

  if s.respond_to? :specification_version then
    s.specification_version = 4

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_development_dependency(%q<rake>, ["~> 10.1"])
      s.add_development_dependency(%q<arachni>, ["~> 0.4"])
      s.add_runtime_dependency(%q<cucumber>, ["= 1.3.11"])
      s.add_runtime_dependency(%q<aruba>, ["= 0.5.4"])
      s.add_runtime_dependency(%q<nokogiri>, ["= 1.6.1"])
      s.add_runtime_dependency(%q<trollop>, ["~> 2.0"])
    else
      s.add_dependency(%q<rake>, ["~> 10.1"])
      s.add_dependency(%q<arachni>, ["~> 0.4"])
      s.add_dependency(%q<cucumber>, ["= 1.3.11"])
      s.add_dependency(%q<aruba>, ["= 0.5.4"])
      s.add_dependency(%q<nokogiri>, ["= 1.6.1"])
      s.add_dependency(%q<trollop>, ["~> 2.0"])
    end
  else
    s.add_dependency(%q<rake>, ["~> 10.1"])
    s.add_dependency(%q<arachni>, ["~> 0.4"])
    s.add_dependency(%q<cucumber>, ["= 1.3.11"])
    s.add_dependency(%q<aruba>, ["= 0.5.4"])
    s.add_dependency(%q<nokogiri>, ["= 1.6.1"])
    s.add_dependency(%q<trollop>, ["~> 2.0"])
  end
end
