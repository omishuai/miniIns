@security
Feature: Authorization
  Scenario: User Is Authorized
    Given empty database
    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
    And User logins with "email@server.com" and "password"
    And User is authenticated
    When User with username "username" visits page "/user/username"
    Then Response has status code 200
    And Response has value "username" for "$.user.username"
    And Response has value "email@server.com" for "$.user.email"
    And Response has value 21 for "$.user.age"
    And Response has value "male" for "$.user.gender"
    And Response has value 0 for "$.photos.length()"


  Scenario: User Is NOT Authorized
    Given empty database
    When User with username "u" visits page "/u"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"
