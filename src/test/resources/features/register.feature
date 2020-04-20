Feature: Create User

  Scenario: Users Register Correctly
    Given empty database
    When User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    Then Response has status code 201
    And Response has value "username" for "$.username"
    And Response has value "email@server.com" for "$.email"
    And Response has value 21 for "$.age"
    And Response has value "male" for "$.gender"

  Scenario: Users Failed to Register Due to Existing Username
    Given User with "username" for "username" exists in database
    When User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    Then Response has status code 409


  Scenario: Users Failed to Register Due to Existing Email
    Given User with "email@server.com" for "email" exists in database
    When User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    Then Response has status code 409

  Scenario: Users Failed to Register Due to Under Age
    Given empty database
    When User registers with username "username",password "password", email "email@server.com", age 17 and gender "male"
    Then Response has status code 400

  Scenario: Users Failed to Register Due to Short Password
    Given empty database
    When User registers with username "username",password "pass", email "email@server.com", age 21 and gender "male"
    Then Response has status code 400

  Scenario: Users Failed to Register Due to Wronly Formatted Email
    Given empty database
    When User registers with username "username",password "password", email "emailserver", age 21 and gender "male"
    Then Response has status code 400