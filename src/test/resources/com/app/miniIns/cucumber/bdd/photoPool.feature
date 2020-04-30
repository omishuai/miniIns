Feature: Photo Pool

  Scenario: User Logs In and See Recent Photos Uploaded by Users
    Given empty database
    And User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    And User logins with "email@server.com" and "password"
    And User is authenticated
    And User with username "username" uploads file "MiniIns/img1.png"

    And User registers with username "username2",password "password", email "email2@server.com", age 21 and gender "male"
    And User logins with "email2@server.com" and "password"
    And User is authenticated
    And User with username "username2" uploads file "MiniIns/img1.png"

    And User registers with username "username3",password "password", email "email3@server.com", age 21 and gender "male"
    And User logins with "email3@server.com" and "password"
    And User is authenticated
    And User with username "username3" uploads file "MiniIns/img1.png"

    And User registers with username "username4",password "password", email "email4@server.com", age 21 and gender "male"
    And User logins with "email4@server.com" and "password"
    And User is authenticated
    When User with username "username4" visits page "/explore"
    Then Response has value 3 for "$.length()"
    And Response has value "username" for "$[0].username"
    And Response has value "username2" for "$[1].username"
    And Response has value "username3" for "$[2].username"

    And Response has status code 200
