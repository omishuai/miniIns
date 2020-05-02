@follow-unfollow
Feature: follow and unfollow

  Scenario: User logs in and follow and then unfollow user
    Given empty database
    And User registers with username "username1",password "password", email "email1@server.com", age 21 and gender "male"
    And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"
    And User logins with "email1@server.com" and "password"
    And User is authenticated

    When User with username "username1" (un)follows "username2" through "/follow"
    Then Response has status code 201
    And Response has value "username2" for "$.user2.username"
    And Response has value "email2@server.com" for "$.user2.email"
    And Response has value 21 for "$.user2.age"
    And Response has value "male" for "$.user2.gender"
    And Response has value 0 for "$.user2.following.length()"
    And Response has value 1 for "$.user2.followedBy.length()"
    And Response has value "username1" for "$.user1.username"
    And Response has value "email1@server.com" for "$.user1.email"
    And Response has value 21 for "$.user1.age"
    And Response has value "male" for "$.user1.gender"
    And Response has value 1 for "$.user1.following.length()"
    And Response has value 0 for "$.user1.followedBy.length()"

    When User with username "username1" (un)follows "username2" through "/unfollow"
    Then Response has status code 201
    And Response has value "username2" for "$.user2.username"
    And Response has value "email2@server.com" for "$.user2.email"
    And Response has value 21 for "$.user2.age"
    And Response has value "male" for "$.user2.gender"
    And Response has value 0 for "$.user2.following.length()"
    And Response has value 0 for "$.user2.followedBy.length()"
    And Response has value "username1" for "$.user1.username"
    And Response has value "email1@server.com" for "$.user1.email"
    And Response has value 21 for "$.user1.age"
    And Response has value "male" for "$.user1.gender"
    And Response has value 0 for "$.user1.following.length()"
    And Response has value 0 for "$.user1.followedBy.length()"


  Scenario: User does not log in to follow/unfollow user
    Given empty database
    When User with username "usr" (un)follows "userna" through "/unfollow"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"
    When User with username "usere1" (un)follows "usere2" through "/follow"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"