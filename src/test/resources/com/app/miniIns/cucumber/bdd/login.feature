#Feature: User Login
#  Scenario: Users Login Successfully Using Username
#    Given empty database
#    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
#    And Response has status code 201
#    When User logins with "username" and "password"
#    Then Response has status code 200
#    And Response has value "username" for "$.username"
#    And Response has value "email@server.com" for "$.email"
#    And Response has value 21 for "$.age"
#    And Response has value "male" for "$.gender"
#
#  Scenario: Users Login Successfully Using Email
#    Given empty database
#    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
#    And Response has status code 201
#    When User logins with "email@server.com" and "password"
#    Then Response has status code 200
#    And Response has value "username" for "$.username"
#    And Response has value "email@server.com" for "$.email"
#    And Response has value 21 for "$.age"
#    And Response has value "male" for "$.gender"
#
#  Scenario: Users Failed to login due to un-matching password
#    Given empty database
#    And User with username "username",password "password", email "email@server.com", age 21 and gender "male" exists
#    And Response has status code 201
#    When User logins with "email@server.com" and "passworddd"
#    Then Response has status code 401
#    And  Response has value "Incorrect Password" for "$.message"
#
#  Scenario: Users Failed to Login Because No Password
#    When User logins with "email@server.com" and ""
#    Then Response has status code 401
#    And  Response has value "Please Enter Password" for "$.message"
#
#  Scenario: Users Failed to Login Because No Email
#    When User logins with "" and "password"
#    Then Response has status code 401
#    And  Response has value "Please Enter Username or Email" for "$.message"
#
#  Scenario: Users Failed to Login Because No Username
#    When User logins with "" and "password"
#    Then Response has status code 401
#    And  Response has value "Please Enter Username or Email" for "$.message"
#
#
#  Scenario: Users Failed to Login Due to unregistered Username
#    Given empty database
#    When User logins with "email@server" and "password"
#    Then Response has status code 401
#    And  Response has value "Unregistered email@server" for "$.message"
#
#  Scenario: Users Failed to Login Due to unregistered Email
#    Given empty database
#    When User logins with "userm" and "password"
#    Then Response has status code 401
#    And  Response has value "Unregistered userm" for "$.message"