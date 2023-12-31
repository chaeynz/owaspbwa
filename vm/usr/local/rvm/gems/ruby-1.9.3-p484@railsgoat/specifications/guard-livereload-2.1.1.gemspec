# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "guard-livereload"
  s.version = "2.1.1"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Thibaud Guillaume-Gentil"]
  s.date = "2014-01-16"
  s.description = "Guard::LiveReload automatically reloads your browser when 'view' files are modified."
  s.email = "thibaud@thibaud.me"
  s.homepage = "https://rubygems.org/gems/guard-livereload"
  s.licenses = ["MIT"]
  s.require_paths = ["lib"]
  s.rubygems_version = "1.8.28"
  s.summary = "Guard plugin for livereload"

  if s.respond_to? :specification_version then
    s.specification_version = 4

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<guard>, ["~> 2.0"])
      s.add_runtime_dependency(%q<em-websocket>, ["~> 0.5"])
      s.add_runtime_dependency(%q<multi_json>, ["~> 1.8"])
      s.add_development_dependency(%q<bundler>, [">= 1.3.5"])
      s.add_development_dependency(%q<rake>, [">= 0"])
      s.add_development_dependency(%q<rspec>, [">= 0"])
    else
      s.add_dependency(%q<guard>, ["~> 2.0"])
      s.add_dependency(%q<em-websocket>, ["~> 0.5"])
      s.add_dependency(%q<multi_json>, ["~> 1.8"])
      s.add_dependency(%q<bundler>, [">= 1.3.5"])
      s.add_dependency(%q<rake>, [">= 0"])
      s.add_dependency(%q<rspec>, [">= 0"])
    end
  else
    s.add_dependency(%q<guard>, ["~> 2.0"])
    s.add_dependency(%q<em-websocket>, ["~> 0.5"])
    s.add_dependency(%q<multi_json>, ["~> 1.8"])
    s.add_dependency(%q<bundler>, [">= 1.3.5"])
    s.add_dependency(%q<rake>, [">= 0"])
    s.add_dependency(%q<rspec>, [">= 0"])
  end
end
