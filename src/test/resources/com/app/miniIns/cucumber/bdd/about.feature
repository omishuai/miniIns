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

    When User with username "username" visits page "/user/username" with page 0 size 2
    Then Response has status code 200
    And Response has value "username" for "$.username"
    And Response has value 2 for "$.photos.length()"
    And Response has value 0 for "$.followsCount"
    And Response has value 0 for "$.followedByCount"
    And Response has value 7 for "$.photosCount"

    When User with username "username" visits page "/user/username" with page 1 size 2
    Then Response has status code 200
    And Response has value "username" for "$.username"
    And Response has value 2 for "$.photos.length()"
    And Response has value 0 for "$.followsCount"
    And Response has value 0 for "$.followedByCount"
    And Response has value 7 for "$.photosCount"

    When User with username "username" visits page "/user/username" with page 1 size 20
    Then Response has status code 400
    And Response has value "Limit 20 Does Not Fall In 0 to 15" for "$.message"

    When User with username "username" visits page "/user/username" with page 1 size -1
    Then Response has status code 400
    And Response has value "Limit -1 Does Not Fall In 0 to 15" for "$.message"

    When User with username "username" visits page "/user/username" with page 1 size 4
    Then Response has status code 200
    And Response has value "username" for "$.username"
    And Response has value 3 for "$.photos.length()"
    And Response has value 0 for "$.followsCount"
    And Response has value 0 for "$.followedByCount"
    And Response has value 7 for "$.photosCount"


  Scenario: User Does not Log In to See Photos in User's Home Page
    Given empty database
    When User with username "useme" visits page "/user/useme" with page 0 size 2
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"
