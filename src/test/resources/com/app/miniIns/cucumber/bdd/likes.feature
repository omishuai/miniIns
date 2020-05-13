@likes
Feature: Likes

  Scenario: User likes a photo
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" likes a photo
    Then Response has status code 201


  Scenario: User removes likes
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" likes a photo
    Then Response has status code 201

    When User "username2" unlikes a photo
    Then Response has status code 201

  Scenario: User likes a already liked photo
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" likes a photo
    Then Response has status code 201

    When User "username2" likes a photo
    Then Response has status code 201

  Scenario: User removes likes that user never liked
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" unlikes a photo
    Then Response has status code 201

