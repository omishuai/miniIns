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
    Given empty database
    And User with username "username",password "password", email "email2@server.com", age 21 and gender "male" exists
    When User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    Then Response has status code 409
    And  Response has value "Existing Username" for "$.message"


  Scenario: Users Failed to Register Due to Existing Email
    Given empty database
    And User with username "userna",password "password", email "email@server.com", age 21 and gender "male" exists
    When User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    Then Response has status code 409
    And  Response has value "Existing Email" for "$.message"

  Scenario: Users Failed to Register Due to Under Age
    Given empty database
    When User registers with username "username",password "password", email "email@server.com", age 17 and gender "male"
    Then Response has status code 400
    And  Response has value "Under Age" for "$.message"

  Scenario: Users Failed to Register Due to Short Password
    Given empty database
    When User registers with username "username",password "pass", email "email@server.com", age 21 and gender "male"
    Then Response has status code 400
    And  Response has value "The Password Is Too Short" for "$.message"

  Scenario: Users Failed to Register Due to Wronly Formatted Email
    Given empty database
    When User registers with username "username",password "password", email "emailserver", age 21 and gender "male"
    Then Response has status code 400
    And Response has value "Invalid Email" for "$.message"