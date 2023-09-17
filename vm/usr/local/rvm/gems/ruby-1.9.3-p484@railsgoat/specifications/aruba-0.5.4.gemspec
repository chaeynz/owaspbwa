# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "aruba"
  s.version = "0.5.4"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Aslak Helles\u{f8}y", "David Chelimsky", "Mike Sassak", "Matt Wynne"]
  s.date = "2014-01-10"
  s.description = "CLI Steps for Cucumber, hand-crafted for you in Aruba"
  s.email = "cukes@googlegroups.com"
  s.homepage = "http://github.com/cucumber/aruba"
  s.licenses = ["MIT"]
  s.rdoc_options = ["--charset=UTF-8"]
  s.require_paths = ["lib"]
  s.rubygems_version = "1.8.28"
  s.summary = "aruba-0.5.4"

  if s.respond_to? :specification_version then
    s.specification_version = 4

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<cucumber>, [">= 1.1.1"])
      s.add_runtime_dependency(%q<childprocess>, [">= 0.3.6"])
      s.add_runtime_dependency(%q<rspec-expectations>, [">= 2.7.0"])
      s.add_development_dependency(%q<bcat>, [">= 0.6.1"])
      s.add_development_dependency(%q<kramdown>, [">= 0.14"])
      s.add_development_dependency(%q<rake>, [">= 0.9.2"])
      s.add_development_dependency(%q<rspec>, [">= 2.11.0"])
      s.add_development_dependency(%q<fuubar>, [">= 1.1.1"])
    else
      s.add_dependency(%q<cucumber>, [">= 1.1.1"])
      s.add_dependency(%q<childprocess>, [">= 0.3.6"])
      s.add_dependency(%q<rspec-expectations>, [">= 2.7.0"])
      s.add_dependency(%q<bcat>, [">= 0.6.1"])
      s.add_dependency(%q<kramdown>, [">= 0.14"])
      s.add_dependency(%q<rake>, [">= 0.9.2"])
      s.add_dependency(%q<rspec>, [">= 2.11.0"])
      s.add_dependency(%q<fuubar>, [">= 1.1.1"])
    end
  else
    s.add_dependency(%q<cucumber>, [">= 1.1.1"])
    s.add_dependency(%q<childprocess>, [">= 0.3.6"])
    s.add_dependency(%q<rspec-expectations>, [">= 2.7.0"])
    s.add_dependency(%q<bcat>, [">= 0.6.1"])
    s.add_dependency(%q<kramdown>, [">= 0.14"])
    s.add_dependency(%q<rake>, [">= 0.9.2"])
    s.add_dependency(%q<rspec>, [">= 2.11.0"])
    s.add_dependency(%q<fuubar>, [">= 1.1.1"])
  end
end
