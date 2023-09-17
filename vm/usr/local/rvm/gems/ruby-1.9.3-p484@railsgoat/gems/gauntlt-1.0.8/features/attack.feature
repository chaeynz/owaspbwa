Feature: Verify the attack behaviour is correct

  In order to ensure attack behaviour is correct,
  As a software developer or security expert,
  I want to run automated attacks that will pass or fail.

  Scenario: List available attack steps
    Given an attack "nmap" exists
    When I run `gauntlt --list`
    Then it should pass with:
      """
      nmap
      """
      
  Scenario: List all available attack steps and aruba steps that are available
    Given an attack "nmap" exists
    And a file named "nmap.attack" with:
      """
      Feature: simplest attack possible
        Scenario:
          When I launch a "generic" attack with:
            \"\"\"
            ls -a
            \"\"\"
          Then the output should contain:
            \"\"\"
            .
            \"\"\"
      """
    When I run `gauntlt --allsteps`
    Then it should pass with:
      """
      the stdout should not contain
      """

  Scenario: List defined step definitions
    Given an attack "nmap" exists
    And a file named "nmap.attack" with:
      """
      Feature: simplest attack possible
        Scenario:
          When I launch a "generic" attack with:
            \"\"\"
            ls -a
            \"\"\"
          Then the output should contain:
            \"\"\"
            .
            \"\"\"
      """
    When I run `gauntlt --steps`
    Then it should pass with:
      """
      /^"nmap" is installed$/
      """

  Scenario: Run attack
    Given an attack "nmap" exists
    And a file named "nmap.attack" with:
      """
      Feature: simplest attack possible
        Scenario:
          When I launch a "generic" attack with:
            \"\"\"
            ls -a
            \"\"\"
          Then the output should contain:
            \"\"\"
            .
            \"\"\"
      """
    When I run `gauntlt`
    Then it should pass with:
      """
      2 steps (2 passed)
      """

  Scenario: Run attack with custom filename
    Given an attack "nmap" exists
    And a file named "my.awesome.attack.file" with:
      """
      Feature: my nmap attacks
        Scenario: nmap attack works
          Given "nmap" is installed
      """
    When I run `gauntlt my.awesome.attack.file`
    Then it should pass with:
      """
      1 step (1 passed)
      """

  Scenario: Run attack with undefined steps
    Given an attack "nmap" exists
    And a file named "nmap.attack" with:
      """
      Feature: my non-existent attack
        Scenario: Fail on undefined step definition
          Given this_attack_would_never_exist 
      """
    When I run `gauntlt`
    Then it should fail with:
      """
      Not a recognized gauntlt attack step
      """

  Scenario: No attack files in default path
    When I run `gauntlt`
    Then it should fail with:
      """
      No files found in path
      """

  Scenario: No attack files in specified path
    When I run `gauntlt apaththatdoesnotexist`
    Then it should fail with:
      """
      No files found in path: apaththatdoesnotexist
      """
