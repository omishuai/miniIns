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
    When User "username2" likes a photo
    Then Response has status code 201

    When User with username "username2" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    When User "username3" likes a photo
    Then Response has status code 201

    When User with username "username3" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" likes a photo
    Then Response has status code 201

    When User with username "username4" uploads file "MiniIns/img1.png"
    Then Response has status code 201
    When User with username "username2" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User with username "username1" visits page "/feed" with page 0 size 1
    Then Response has status code 200
    And Response has value 1 for "$.size()"
    And Response has value "username1" for "$.[0].username"
    And Response has value 1 for "$.[0].likedByCount"

    When User with username "username1" (un)follows "username3" through "/user/username3/follow"
    Then Response has status code 201
    When User with username "username1" (un)follows "username2" through "/user/username2/follow"
    Then Response has status code 201
    When User with username "username1" (un)follows "username4" through "/user/username4/follow"
    Then Response has status code 201

    When User with username "username1" visits page "/feed" with page 0 size 5
    Then Response has status code 200
    And Response has value 5 for "$.size()"

    And Response has value "username2" for "$.[0].username"
    And Response has value 0 for "$.[0].likedByCount"
    And Response has value "username4" for "$.[1].username"
    And Response has value 0 for "$.[1].likedByCount"

    And Response has value "username3" for "$.[2].username"
    And Response has value 1 for "$.[2].likedByCount"
    And Response has value "username2" for "$.[2].likedByFollows.[0]"

    And Response has value "username2" for "$.[3].username"
    And Response has value 1 for "$.[3].likedByCount"
    And Response has value "username3" for "$.[3].likedByFollows.[0]"

    And Response has value "username1" for "$.[4].username"
    And Response has value 1 for "$.[4].likedByCount"


    When User with username "username1" (un)follows "username3" through "/user/username3/unfollow"
    Then Response has status code 201

    When User with username "username2" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User with username "username1" visits page "/feed" with page 0 size 5
    Then Response has status code 200
    And Response has value 5 for "$.size()"

    And Response has value "username2" for "$.[0].username"

    And Response has value "username2" for "$.[1].username"

    And Response has value "username4" for "$.[2].username"

    And Response has value "username2" for "$.[3].username"
    And Response has value 1 for "$.[3].likedByCount"
    And Response has value 0 for "$.[3].likedByFollows.size()"

    And Response has value "username1" for "$.[4].username"
    And Response has value 1 for "$.[4].likedByCount"

    When User with username "username1" visits page "/feed" with page 0 size 2
    Then Response has status code 200
    And Response has value 2 for "$.size()"

    And Response has value "username2" for "$.[0].username"
    And Response has value "username2" for "$.[1].username"

    When User with username "username1" visits page "/feed" with page 1 size 2
    Then Response has status code 200
    And Response has value 2 for "$.size()"
    And Response has value "username4" for "$.[0].username"
    And Response has value "username2" for "$.[1].username"

    When User with username "username1" (un)follows "username2" through "/user/username2/unfollow"
    Then Response has status code 201

    When User with username "username4" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User with username "username1" visits page "/feed" with page 0 size 5
    Then Response has status code 200
    And Response has value 3 for "$.size()"
    And Response has value "username4" for "$.[0].username"
    And Response has value "username4" for "$.[1].username"
    And Response has value "username1" for "$.[2].username"