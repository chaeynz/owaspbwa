# Ruby Advisory Database

The Ruby advisory database seeks to compile all advisories relevant to Ruby libraries.

## Directory Structure

The database is a list of directories that match the names of Ruby libraries on
[rubygems.org]. Within each directory are one or more advisory files
for the Ruby library. These advisory files are typically named using
the advisories [CVE] identifier number.

    gems/:
      rails/:
        2012-1098.yml  2012-2660.yml  2012-2661.yml  2012-3463.yml

If an advisory does not yet have a [CVE], [requesting a CVE][1] is easy.
## Format

Each advisory file contains the advisory information in [YAML] format:

    ---
    gem: rails
    cve: 2013-0156
    url: http://osvdb.org/show/osvdb/89026
    title: |
      Ruby on Rails params_parser.rb Action Pack Type Casting Parameter Parsing
      Remote Code Execution 
    
    description: |
      Ruby on Rails contains a flaw in params_parser.rb of the Action Pack.
      The issue is triggered when a type casting error occurs during the parsing
      of parameters. This may allow a remote attacker to potentially execute
      arbitrary code.
    
    cvss_v2: 10.0
    
    patched_versions:
      - ~> 2.3.15
      - ~> 3.0.19
      - ~> 3.1.10
      - ">= 3.2.11"

### Schema

* `gem` \[String\]: Name of the affected gem.
* `cve` \[String\]: CVE id
* `url` \[String\]: The URL to the full advisory.
* `title` \[String\]: The title of the advisory.
* `description` \[String\]: Multi-paragraph description of the vulnerability.
* `cvss_v2` \[Float\]: The [CVSSv2] score for the vulnerability.
* `patched_versions` \[Array\<String\>\]: The version requirements for the
  patched versions of the Ruby library.

## Credits

* [Postmodern](https://github.com/postmodern/)
* [Max Veytsman](https://twitter.com/mveytsman)

[rubygems.org]: https://rubygems.org/
[CVE]: http://cve.mitre.org/
[CVSSv2]: http://www.first.org/cvss/cvss-guide.html
[YAML]: http://www.yaml.org/

[1]: http://people.redhat.com/kseifrie/CVE-OpenSource-Request-HOWTO.html
