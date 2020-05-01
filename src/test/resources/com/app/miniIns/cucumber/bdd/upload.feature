@upload
Feature: upload
  Scenario: User Signed In With Email Uploads an image to Cloud Storage
    Given empty database
    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
    And User logins with "email@server.com" and "password"
    And User is authenticated
    When User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And Response has value "username" for "$.username"
    And Response contains value for "$.url"
    And Response contains value for "$.uuid"

  Scenario: User Signed In With Username Uploads an image to Cloud Storage
    Given empty database
    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
    And User logins with "username" and "password"
    And User is authenticated
    When User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And Response has value "username" for "$.username"
    And Response contains value for "$.url"
    And Response contains value for "$.uuid"

  Scenario: User Does not Log In to Upload
    Given empty database
    And User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    When User logins with "email@server.com" and "passworddd"
    Then Response has status code 401
    And  Response has value "Incorrect Password" for "$.message"
    When User with username "us" uploads file "MiniIns/img1.png"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"

  Scenario: Unregistered User Fails to Upload
    Given empty database
    When User logins with "emailstar@server" and "password"
    Then Response has status code 401
    And  Response has value "Unregistered emailstar@server" for "$.message"
    When User with username "uer" uploads file "MiniIns/img1.png"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"
