Feature: Authorization
  Scenario: User Is Authorized
    Given empty database
    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
    And User logins with "email" "email@server.com" and "password"
    And User with "email" "email@server.com" is authenticated
    When User with username "username" visits page "/{user}"
    Then Response has status code 200
    And Response has value "username" for "$.username"
    And Response has value "email@server.com" for "$.email"
    And Response has value 21 for "$.age"
    And Response has value "male" for "$.gender"


  Scenario: User Is NOT Authorized
    Given empty database
    When User with username "u" visits page "/{user}"
    Then Response has status code 403
#    And Response has value "username" for "$.username"
#    And Response has value "email@server.com" for "$.email"
#    And Response has value 21 for "$.age"
#    And Response has value "male" for "$.gender"