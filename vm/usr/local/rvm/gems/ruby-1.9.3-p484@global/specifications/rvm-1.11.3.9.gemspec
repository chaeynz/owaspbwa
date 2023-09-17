# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "rvm"
  s.version = "1.11.3.9"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Wayne E. Seguin", "Michal Papis"]
  s.date = "2014-01-28"
  s.description = "RVM ~ Ruby Environment Manager ~ Ruby Gem Library."
  s.email = ["wayneeseguin@gmail.com", "mpapis@gmail.com"]
  s.homepage = "http://rvm.io/"
  s.licenses = ["MIT"]
  s.require_paths = ["lib"]
  s.rubygems_version = "1.8.28"
  s.summary = "RVM Ruby Gem Library"

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
    else
    end
  else
  end
end
