@comment
Feature: commenting on photos

  Scenario: user comments on a photo
    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    When User with username "username1" uploads file "MiniIns/img1.png"
    Then Response has status code 201

    ##record the id of the comment
    When User "username2" comments "message1" on photo
    Then Response has status code 201
#    And Response has value 1 for "$.comments.size()"
#    And Response has value "username2" for "$.comments[0].from"
#    And Response has value "message1" for "$.comments[0].text"
#
#    When User "username1" comments "message2" on photo
#    Then Response has status code 201
#    And Response has value 2 for "$.comments.size()"
#    And Response has value "username1" for "$.comments[0].from"
#    And Response has value "message2" for "$.comments[0].text"

#  Scenario: user comments on a photo, and the owner of the photo replies
#    Given empty database
#    And User with username "username1" registers and logs in
#    And User with username "username2" registers and logs in
#
#    When User with username "username1" uploads file "MiniIns/img1.png"
#    Then Response has status code 201
#
#        ##record the id of the comment
#    When User "username2" comments "message1" on photo
#    Then Response has status code 201
#    And Response has value 1 for "$.comments.size()"
#    And Response has value "username2" for "$.comments[0].from"
#    And Response has value "message1" for "$.comments[0].text"
#
#    When User "username1" responds "re:message1" to comment
#    Then Response has status code 201
#    And Response has value 2 for "$.comments.size()"
#    And Response has value "username2" for "$.comments[0].from"
#    And Response has value "message1" for "$.comments[0].text"
#    And Response has value "username1" for "$.comments[1].from"
#    And Response has value "re:message1" for "$.comments[1].text"
#    And Response responds to a comment "$.comments[1].to"
#
#
#
