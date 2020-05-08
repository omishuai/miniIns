@home
Feature: home
  Scenario: display followed people's photos based on time
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in
    And User with username "username3" registers and logs in
    And User with username "username4" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    When User with username "username2" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    When User with username "username3" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    When User with username "username4" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    When User with username "username2" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User with username "username1" visits page "/feed"
    Then Response has status code 200
    And Response has value 1 for "$.photos.size()"
    And Response has value "username1" for "$.photos[0].username"

    When User with username "username1" (un)follows "username3" through "/follow"
    Then Response has status code 201
    When User with username "username1" (un)follows "username2" through "/follow"
    Then Response has status code 201
    When User with username "username1" (un)follows "username4" through "/follow"
    Then Response has status code 201

    When User with username "username1" visits page "/feed"
    Then Response has status code 200
    And Response has value 5 for "$.photos.size()"
    And Response has value "username2" for "$.photos[0].username"
    And Response has value "username4" for "$.photos[1].username"
    And Response has value "username3" for "$.photos[2].username"
    And Response has value "username2" for "$.photos[3].username"
    And Response has value "username1" for "$.photos[4].username"


    When User with username "username1" (un)follows "username3" through "/unfollow"
    Then Response has status code 201

    When User with username "username2" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User with username "username1" visits page "/feed"
    Then Response has status code 200
    And Response has value 5 for "$.photos.size()"

    And Response has value "username2" for "$.photos[0].username"
    And Response has value "username2" for "$.photos[1].username"
    And Response has value "username4" for "$.photos[2].username"
    And Response has value "username2" for "$.photos[3].username"
    And Response has value "username1" for "$.photos[4].username"

    When User with username "username1" (un)follows "username2" through "/unfollow"
    Then Response has status code 201

    When User with username "username4" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User with username "username1" visits page "/feed"
    Then Response has status code 200
    And Response has value 3 for "$.photos.size()"
    And Response has value "username4" for "$.photos[0].username"
    And Response has value "username4" for "$.photos[1].username"
    And Response has value "username1" for "$.photos[2].username"