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

    Scenario: User opens websocket to server, then second one, and tests that the first one was closed by the server
      Given empty database
      And User with username "username1" registers and logs in
      When User with username "username1" opens a socket to "/message" named "websocket1"
      And User with username "username1" opens a socket to "/message" named "websocket2"
      Then "websocket1" is close
      And  "websocket2" is open

    Scenario: User1 sends message to another user2 (both online), but user2 does not "ack" the message. Instead opens a second connection and tests that it gets the message.
      Given empty database
      And User with username "username1" registers and logs in
      And User with username "username2" registers and logs in
      And User with username "username1" opens a socket to "/message" named "websocket1"
      And User with username "username2" opens a socket to "/message" named "websocket2"

      When  User sends message "hello there" to "username2" through socket "websocket1"
      Then consume message from websocket "websocket1"
      And message has "ack" for "$.type"

      And User with username "username2" opens a socket to "/message" named "websocket3"
      And consume message from websocket "websocket3"
      And message has "message" for "$.type"
      And message has "hello there" for "$.message"
      And message has "username1" for "$.sender"
      And message has "username2" for "$.receiver"

    Scenario: User1 sends message to user2 (offline), user2 connects gets the message, but does not "ack", opens second connection and tests it gets the message.
      Given empty database
      And User with username "username1" registers and logs in
      And User with username "username2" registers and logs in
      And User with username "username1" opens a socket to "/message" named "websocket1"

      When  User sends message "hello there" to "username2" through socket "websocket1"
      Then consume message from websocket "websocket1"
      And message has "ack" for "$.type"

      When User with username "username2" opens a socket to "/message" named "websocket2"
      And consume message from websocket "websocket2"
      And message has "message" for "$.type"
      And message has "hello there" for "$.message"
      And message has "username1" for "$.sender"
      And message has "username2" for "$.receiver"

      And User with username "username2" opens a socket to "/message" named "websocket3"
      And consume message from websocket "websocket3"
      And message has "message" for "$.type"
      And message has "hello there" for "$.message"
      And message has "username1" for "$.sender"
      And message has "username2" for "$.receiver"

    Scenario: User1 sends message to a user2 with various input error
      Given empty database
      And User with username "username1" registers and logs in
      And User with username "username1" opens a socket to "/message" named "websocket1"

#     unregistered recipient
      When  User sends message "hello there" to "username2" through socket "websocket1"
      Then consume message from websocket "websocket1"
      And message has "error" for "$.type"
      And message has "Recipient username2 Is Not Found" for "$.message"

#     empty recipient
      When  User sends message "hello there" to "" through socket "websocket1"
      Then consume message from websocket "websocket1"
      And message has "error" for "$.type"
      And message has "Recipient  Is Not Found" for "$.message"

#      empty text
      And User with username "username2" registers and logs in
      When  User sends message "" to "username2" through socket "websocket1"
      Then consume message from websocket "websocket1"
      And message has "error" for "$.type"
      And message has "Invalid Text From User. Text: " for "$.message"

#     wrong message id when ack-ing
      And User with username "username2" opens a socket to "/message" named "websocket2"
      When  User sends message "valid text" to "username2" through socket "websocket1"
      Then consume message from websocket "websocket1"
      And message has "ack" for "$.type"
      When consume message from websocket "websocket2"
      Then message has "message" for "$.type"
      And message has "valid text" for "$.message"
      When User sends ack to websocket "websocket2" with wrong id 100
      When consume message from websocket "websocket2"
      And message has "error" for "$.type"
      And message has "Invalid Message ID 100" for "$.message"

#     send to sender self
      When  User sends message "valid text 2" to "username1" through socket "websocket1"
      When consume message from websocket "websocket1"
      And message has "error" for "$.type"
      And message has "Sender username1 Cannot Send to Recipient username1" for "$.message"


     Scenario: User1 sends message to user2 (online),gets  the message and acks it. Then closes websocket and opens a new one - tests that it gets no messages.
       Given empty database
       And User with username "username1" registers and logs in
       And User with username "username2" registers and logs in
       And User with username "username1" opens a socket to "/message" named "websocket1"
       And User with username "username2" opens a socket to "/message" named "websocket2"

        When  User sends message "hello there" to "username2" through socket "websocket1"
        Then consume message from websocket "websocket1"
        And message has "ack" for "$.type"

        When consume message from websocket "websocket2"
        Then message has "message" for "$.type"
        And message has "hello there" for "$.message"
        And message has "username1" for "$.sender"
        And message has "username2" for "$.receiver"
       When User sends ack to websocket "websocket2"

       And User with username "username2" opens a socket to "/message" named "websocket3"
        And consume message from websocket "websocket3"
        Then there is no message

       Scenario: User1 sends message to user2 (offline), connects gets message, acks, disconnect and connect again - no message.
         Given empty database
         And User with username "username1" registers and logs in
         And User with username "username2" registers and logs in
         And User with username "username1" opens a socket to "/message" named "websocket1"

         When  User sends message "hello there" to "username2" through socket "websocket1"
        Then consume message from websocket "websocket1"
        And message has "ack" for "$.type"

        When User with username "username2" opens a socket to "/message" named "websocket2"
        And consume message from websocket "websocket2"
        And message has "message" for "$.type"
        And message has "hello there" for "$.message"
        And message has "username1" for "$.sender"
        And message has "username2" for "$.receiver"
         When User sends ack to websocket "websocket2"

         When User with username "username2" opens a socket to "/message" named "websocket3"
         And consume message from websocket "websocket3"
         Then there is no message

         Scenario:test with 3 users sending messages between each pair.
           Given empty database
           And User with username "username1" registers and logs in
           And User with username "username2" registers and logs in
           And User with username "username3" registers and logs in

           And User with username "username1" opens a socket to "/message" named "websocket1"
           And User with username "username2" opens a socket to "/message" named "websocket2"
           And User with username "username3" opens a socket to "/message" named "websocket3"

           When  User sends message "hello there 2" to "username2" through socket "websocket1"
           Then consume message from websocket "websocket1"
           And message has "ack" for "$.type"



           When  User sends message "hello there 3" to "username3" through socket "websocket1"
           Then consume message from websocket "websocket1"
           And message has "ack" for "$.type"

           And consume message from websocket "websocket2"
           And message has "message" for "$.type"
           And message has "hello there 2" for "$.message"
           And message has "username1" for "$.sender"
           And message has "username2" for "$.receiver"
           When User sends ack to websocket "websocket2"

           And consume message from websocket "websocket3"
           And message has "message" for "$.type"
           And message has "hello there 3" for "$.message"
           And message has "username1" for "$.sender"
           And message has "username3" for "$.receiver"
           When User sends ack to websocket "websocket3"

           When  User sends message "hello there 1" to "username1" through socket "websocket3"
           Then consume message from websocket "websocket3"
           And message has "ack" for "$.type"

           And consume message from websocket "websocket1"
           And message has "message" for "$.type"
           And message has "hello there 1" for "$.message"
           And message has "username3" for "$.sender"
           And message has "username1" for "$.receiver"
           When User sends ack to websocket "websocket1"

           When  User sends message "hello there 2" to "username2" through socket "websocket3"
           Then consume message from websocket "websocket3"
           And message has "ack" for "$.type"

           And consume message from websocket "websocket2"
           And message has "message" for "$.type"
           And message has "hello there 2" for "$.message"
           And message has "username3" for "$.sender"
           And message has "username2" for "$.receiver"
           When User sends ack to websocket "websocket2"

           When  User sends message "hello there 3" to "username3" through socket "websocket2"
           Then consume message from websocket "websocket2"
           And message has "ack" for "$.type"

           And consume message from websocket "websocket3"
           And message has "message" for "$.type"
           And message has "hello there 3" for "$.message"
           And message has "username2" for "$.sender"
           And message has "username3" for "$.receiver"
           When User sends ack to websocket "websocket3"

           When  User sends message "hello there 1" to "username1" through socket "websocket2"
           Then consume message from websocket "websocket2"
           And message has "ack" for "$.type"

           And consume message from websocket "websocket1"
           And message has "message" for "$.type"
           And message has "hello there 1" for "$.message"
           And message has "username2" for "$.sender"
           And message has "username1" for "$.receiver"
           When User sends ack to websocket "websocket1"
