# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "guard-brakeman"
  s.version = "0.8.1"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Neil Matatall", "Justin Collins"]
  s.date = "2013-08-28"
  s.description = "Guard::Brakeman automatically scans your Rails app for vulnerabilities using the Brakeman Scaner https://github.com/presidentbeef/brakeman"
  s.homepage = "https://github.com/guard/guard-brakeman"
  s.licenses = ["MIT"]
  s.rdoc_options = ["--charset=UTF-8", "--main=README.md", "--exclude='(test|spec)|(Gem|Guard|Rake)file'"]
  s.require_paths = ["lib"]
  s.rubyforge_project = "guard-brakeman"
  s.rubygems_version = "1.8.28"
  s.summary = "Guard gem for Brakeman"

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<guard>, [">= 1.1.0"])
      s.add_runtime_dependency(%q<brakeman>, [">= 2.1.1"])
    else
      s.add_dependency(%q<guard>, [">= 1.1.0"])
      s.add_dependency(%q<brakeman>, [">= 2.1.1"])
    end
  else
    s.add_dependency(%q<guard>, [">= 1.1.0"])
    s.add_dependency(%q<brakeman>, [">= 2.1.1"])
  end
end
