@chat
Feature: Chat

  Scenario: User1 （Connected) Sends A Message to User2 (Connected)

    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in
    And User with username "username1" opens a socket to "/message" named "websocket1"
    And User with username "username2" opens a socket to "/message" named "websocket2"

#    Message is sent to user2
    When  User sends message "hello there" to "username2" through socket "websocket1"

#    From socket1 => ack message from handler； This is just to get the message, and store it before performing a series of checking
    Then consume message from websocket "websocket1"
    And message has "ack" for "$.type"

#    From socket2 => message from user1
    Then consume message from websocket "websocket2"
    And message has "message" for "$.type"
    And message has "hello there" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

#     Send ack to handler to signify the message has been received
    And User sends ack to websocket "websocket2"


  Scenario: User1 (Connected) Sends Multiple Messages to User2 (Disconnected, Then Connected)

    Given empty database
    And User with username "username1" registers and logs in
    And User with username "username2" registers and logs in

    And User with username "username1" opens a socket to "/message" named "websocket1"

#   Message is sent to user2
    When  User sends message "hello there" to "username2" through socket "websocket1"
    Then consume message from websocket "websocket1"
    And message has "ack" for "$.type"
    When  User sends message "hello there message 2" to "username2" through socket "websocket1"
    Then consume message from websocket "websocket1"
    And message has "ack" for "$.type"

    And User with username "username2" opens a socket to "/message" named "websocket2"

#   From socket2 => message from user1
    Then consume message from websocket "websocket2"
    And message has "message" for "$.type"
    And message has "hello there" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

#   Send ack to handler to signify the message has been received
    When User sends ack to websocket "websocket2"

#  From socket2 => message from user1
    And consume message from websocket "websocket2"
    And message has "message" for "$.type"
    And message has "hello there message 2" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

    # Send ack to handler to signify the message has been received
    When User sends ack to websocket "websocket2"