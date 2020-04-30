Feature: User Home

  Scenario: User Logs In and See all Photos in User's Home Page
    Given empty database
    And User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    And User logins with "email@server.com" and "password"
    And User is authenticated
    And User with username "username" uploads file "MiniIns/img1.png"
    And User with username "username" uploads file "MiniIns/img1.png"
    And User with username "username" uploads file "MiniIns/img1.png"
    And User with username "username" uploads file "MiniIns/img1.png"
    And User with username "username" uploads file "MiniIns/img1.png"
    And User with username "username" uploads file "MiniIns/img1.png"
    And User with username "username" uploads file "MiniIns/img1.png"
    When User with username "username" visits page "/username"

    Then Response has status code 200
    And Response has value "username" for "$.user.username"
    And Response has value "email@server.com" for "$.user.email"
    And Response has value 21 for "$.user.age"
    And Response has value "male" for "$.user.gender"
    And Response contains 7 photos for "$.photos"


  Scenario: User Does not Log In to See Photos in User's Home Page
    Given empty database
    And User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    When User logins with "email@server.com" and "passworddd"
    Then Response has status code 401
    And  Response has value "Incorrect Password" for "$.message"
    When User with username "us" uploads file "MiniIns/img1.png"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"

  Scenario: Unregistered User Fails to See Photos in User's Home Page
    Given empty database
    When User logins with "email@server" and "password"
    Then Response has status code 401
    And  Response has value "Unregistered email@server" for "$.message"
    When User with username "uer" uploads file "MiniIns/img1.png"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"