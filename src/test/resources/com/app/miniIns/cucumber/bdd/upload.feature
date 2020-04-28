Feature: upload
  Scenario: User Signed In With Email Uploads an image to Cloud Storage
    Given empty database
    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
    And User logins with "email" "email@server.com" and "password"
    And User with "email" "email@server.com" is authenticated
    When User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201

  Scenario: User Signed In With Username Uploads an image to Cloud Storage
#    Given empty database
#    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
    And User logins with "username" "username" and "password"
    And User with "username" "username" is authenticated
    When User with username "username" uploads file "MiniIns/img1.png"
    Then Response has status code 201