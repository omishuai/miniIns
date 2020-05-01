@security
Feature: Authorization
  Scenario: User Is Authorized
    Given empty database
    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
    And User logins with "email@server.com" and "password"
    And User is authenticated
    When User with username "username" visits page "/username"



  Scenario: User Is NOT Authorized
    Given empty database
    When User with username "u" visits page "/u"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"
