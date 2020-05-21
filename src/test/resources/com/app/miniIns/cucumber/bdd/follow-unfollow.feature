@follow-unfollow
Feature: follow and unfollow

  Scenario: User logs in and follow and then unfollow user
    Given empty database
    And User registers with username "username1",password "password", email "email1@server.com", age 21 and gender "male"
    And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"
    And User logins with "email1@server.com" and "password"
    And User is authenticated

    When User with username "username1" (un)follows "username2" through "/user/username2/follow"
    Then Response has status code 201

#==================================================================
    And User logins with "email2@server.com" and "password"
    And User is authenticated
    When User with username "username2" (un)follows "username1" through "/user/username1/follow"
    Then Response has status code 201
#==================================================================
    When User with username "username2" (un)follows "username1" through "/user/username1/unfollow"
    Then Response has status code 201



  Scenario: User logs in and follow user two times
    Given empty database
    And User registers with username "username1",password "password", email "email1@server.com", age 21 and gender "male"
    And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"

    And User logins with "email1@server.com" and "password"
    And User is authenticated

    When User with username "username1" (un)follows "username2" through "/user/username2/follow"
    Then Response has status code 201

    When User with username "username1" (un)follows "username2" through "/user/username2/follow"
    Then Response has status code 201

  Scenario: User logs in and unfollows before follows the user
    Given empty database
    And User registers with username "username1",password "password", email "email1@server.com", age 21 and gender "male"
    And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"
    And User registers with username "username3",password "password", email "email3@server.com", age 21 and gender "male"

    And User logins with "email1@server.com" and "password"
    And User is authenticated

    When User with username "username1" (un)follows "username3" through "/user/username3/follow"
    Then Response has status code 201


    And User logins with "email2@server.com" and "password"
    And User is authenticated
    When User with username "username2" (un)follows "username3" through "/user/username3/unfollow"
    Then Response has status code 201


  Scenario: User does not log in to follow/unfollow user
    Given empty database
    When User with username "usr" (un)follows "userna" through "/user/userna/unfollow"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"
    When User with username "usere1" (un)follows "usere2" through "/user/usere2/follow"
    Then Response has status code 403
    And Response has value "Access Denied" for "$.message"