@likes
Feature: Likes

  Scenario: User likes a photo
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And Response has value "username1" for "$.username"
    And Response contains value for "$.url"
    And Response contains value for "$.uuid"

    When User "username2" likes a photo
    Then Response has status code 201
    And Response has value 1 for "$.likedBy.size()"
    And Response has value "username2" for "$.likedBy[0].username"


  Scenario: User removes likes
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    And Response has value "username1" for "$.username"
    And Response contains value for "$.url"
    And Response contains value for "$.uuid"

    When User "username2" likes a photo
    Then Response has status code 201
    And Response has value 1 for "$.likedBy.size()"
    And Response has value "username2" for "$.likedBy[0].username"

    When User "username2" unlikes a photo
    Then Response has status code 201
    And Response has value 0 for "$.likedBy.size()"


