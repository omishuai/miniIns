@chat
Feature: Chat

  Scenario: User1 （Connected) Sends A Message to User2 (Connected)

    Given empty database
    And User registers with username "username1",password "password", email "email1@server.com", age 21 and gender "male"
    And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"

    And User logins with "email1@server.com" and "password"
    And User is authenticated
    And User with username "username1" opens a socket to "/message" named "websocket1"

    And User logins with "email2@server.com" and "password"
    And User is authenticated
    And User with username "username2" opens a socket to "/message" named "websocket2"

#    Message is sent to user2
    When  User sends message "hello there" to "username2" through socket "websocket1"

    #Testing the messages consumed from both sockets
    #From socket1 => ack message from handler； This is just to get the message, and store it before performing a series of checking
    Then consume message from websocket "websocket1"
    And message has "ack" for "$.type"
    And message has "has been Received on Server" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

    #From socket2 => message from user1
    Then consume message from websocket "websocket2"
    And message has "message" for "$.type"
    And message has "hello there" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

    # Send ack to handler to signify the message has been received
    When User sends ack to websocket "websocket2"


  Scenario: User1 (Connected) Sends Multiple Messages to User2 (Disconnected, Then Connected)

    Given empty database
    And User registers with username "username1",password "password", email "email1@server.com", age 21 and gender "male"
    And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"

    And User logins with "email1@server.com" and "password"
    And User is authenticated
    And User with username "username1" opens a socket to "/message" named "websocket1"

#    Message is sent to user2
    When  User sends message "hello there" to "username2" through socket "websocket1"
    #From socket1 => ack message from handler； This is just to get the message, and store it before performing a series of checking
    Then consume message from websocket "websocket1"
    And message has "ack" for "$.type"
    And message has "has been Received on Server" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

    When  User sends message "hello there message 2" to "username2" through socket "websocket1"
    #From socket1 => ack message from handler； This is just to get the message, and store it before performing a series of checking
    Then consume message from websocket "websocket1"
    And message has "ack" for "$.type"
    And message has "has been Received on Server" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

    When User logins with "email2@server.com" and "password"
    And User is authenticated
    And User with username "username2" opens a socket to "/message" named "websocket2"

    #From socket2 => message from user1
    Then consume message from websocket "websocket2"
    And message has "message" for "$.type"
    And message has "hello there" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"
        # Send ack to handler to signify the message has been received
    When User sends ack to websocket "websocket2"

    #sleep() has been added before another message is sent from severver to client2 using websocket2
#  From socket2 => message from user1
    And consume message from websocket "websocket2"
    And message has "message" for "$.type"
    And message has "hello there message 2" for "$.message"
    And message has "username1" for "$.sender"
    And message has "username2" for "$.receiver"

    # Send ack to handler to signify the message has been received
    When User sends ack to websocket "websocket2"


#    Scenario: User1 and User2 are both online, while User1 is sending message to User2, User2 is offline.
#
#      Given empty database
#      And User registers with username "username1",password "password", email "email1@server.com", age 21 and gender "male"
#      And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"
#
#      And User logins with "email1@server.com" and "password"
#      And User is authenticated
#      And User with username "username1" opens a socket to "/message" named "websocket1"
#
#      And User logins with "email2@server.com" and "password"
#      And User is authenticated
#      And User with username "username2" opens a socket to "/message" named "websocket2"
#
##    Message is sent to user2
#      When  User sends message "hello there" to "username2" through socket "websocket1"
#    #From socket1 => ack message from handler； This is just to get the message, and store it before performing a series of checking
#      Then consume message from websocket "websocket1"
#      And message has "ack" for "$.type"
#      And message has "has been Received on Server" for "$.message"
#      And message has "username1" for "$.sender"
#      And message has "username2" for "$.receiver"
#      And Message Table contains 1 entry
#
#
#
#      And "websocket2" disconnects
#
#      When  User sends message "hello there message 2" to "username2" through socket "websocket1"
#    #From socket1 => ack message from handler； This is just to get the message, and store it before performing a series of checking
#      Then consume message from websocket "websocket1"
#      And message has "ack" for "$.type"
#      And message has "has been Received on Server" for "$.message"
#      And message has "username1" for "$.sender"
#      And message has "username2" for "$.receiver"
#      And Message Table contains 2 entry
#
#      When consume message from websocket "websocket2"
#      And message has "message" for "$.type"
#      And message has "hello there" for "$.message"
#      And message has "username1" for "$.sender"
#      And message has "username2" for "$.receiver"
#
#    # Send ack to handler to signify the message has been received
#      When User sends ack to websocket "websocket2"
#      And Message Table contains 0 entry