@about
Feature: User Home

  Scenario: User Logs In and See all Photos in User's Home Page
    Given empty database
    And User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    And User logins with "email@server.com" and "password"
    And User is authenticated
    And User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User with username "username" visits page "/user/username/about"
    Then Response has status code 200
    And Response has value "username" for "$.user.username"
    And Response has value "email@server.com" for "$.user.email"
    And Response has value 21 for "$.user.age"
    And Response has value "male" for "$.user.gender"
    And Response has value 7 for "$.photos.length()"


  Scenario: User Does not Log In to See Photos in User's Home Page
    Given empty database
    When User with username "useme" visits page "/user/useme/about"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"
