@photoComment
Feature: commenting on photos

  Scenario: user or owner comments on a photo
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    ##record the id of the comment
    When User "username2" comments "message1" on photo
    Then Response has status code 201
    And Response has value 1 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"

    When User "username1" comments "message2" on photo
    Then Response has status code 201
    And Response has value 2 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"
    And Response has value "username1" for "$.photoComments[1].fromUser"
    And Response has value "message2" for "$.photoComments[1].text"

  Scenario: user comments on a photo, and the owner of the photo replies
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

        ##record the id of the comment
    When User "username2" comments "message1" on photo
    Then Response has status code 201
    And Response has value 1 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"

    When User "username1" responds "re:message1" to comment

    Then Response has status code 201
    And Response has value 2 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"
    And Response has value "username1" for "$.photoComments[1].fromUser"
    And Response has value "re:message1" for "$.photoComments[1].text"
    And Response responds to a comment with id for "$.photoComments[1].toId"

  Scenario: User comments without a message
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" comments "" on photo
    Then Response has status code 400
    And Response has value "Text Is Empty" for "$.message"

    When User "username1" comments "" on photo
    Then Response has status code 400
    And Response has value "Text Is Empty" for "$.message"


  Scenario: User (no owner) comments another user's comment (not owner) => fail
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in
    And User with username "username3" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" comments "message1" on photo
    Then Response has status code 201
    And Response has value 1 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"

    When User "username1" responds "re:message1" to comment
    Then Response has status code 201
    And Response has value 2 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"
    And Response has value "username1" for "$.photoComments[1].fromUser"
    And Response has value "re:message1" for "$.photoComments[1].text"
    And Response responds to a comment with id for "$.photoComments[1].toId"

    # Comment on a comment that is not sent to the user
    When User "username3" responds "re:message2" to comment
    Then Response has status code 201
    And Response has value 2 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"
    And Response has value "username1" for "$.photoComments[1].fromUser"
    And Response has value "re:message1" for "$.photoComments[1].text"


  Scenario: User (no owner) comments no existing user's comment  => fail
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in
    And User with username "username3" registers and logs in


    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    When User "username2" comments "message1" on photo
    Then Response has status code 201
    And Response has value 1 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"

    When User "username1" responds "re:message1" to comment
    Then Response has status code 201
    And Response has value 2 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[0].fromUser"
    And Response has value "message1" for "$.photoComments[0].text"
    And Response has value "username1" for "$.photoComments[1].fromUser"
    And Response has value "re:message1" for "$.photoComments[1].text"
    And Response responds to a comment with id for "$.photoComments[1].toId"

    When User "username2" responds "re:message3" to comment
    Then Response has status code 201
    And Response has value 3 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[2].fromUser"
    And Response has value "re:message3" for "$.photoComments[2].text"
    And Response responds to a comment with id for "$.photoComments[2].toId"

    #
    When User "username2" responds "re:message4" to comment with id 100
    Then Response has status code 201
    And Response has value 3 for "$.photoComments.size()"
    And Response has value "username2" for "$.photoComments[2].fromUser"
    And Response has value "re:message3" for "$.photoComments[2].text"

#
#
#
