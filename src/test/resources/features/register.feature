Feature: Create User
  Scenario: Users Register Correctly
    When User registers with username "username",password "password", email "email@server.com", age 21 and gender "male"
    Then Response has status code 201
    And Response has value "username" for "/username"
    And Response has value "email@server.com" for "/email"
    And Response has value 21 for "/age"
    And Response has value "male" for "/gender"

#    Given user wants to create an employee with the following attributes
#      | id  | firstName | lastName | dateOfBirth | startDate  | employmentType | email               |
#      | 100 | Rachel    | Green    | 1990-01-01  | 2018-01-01 | Permanent      | rachel.green@fs.com |
#
#    And with the following phšone numbers
#      | id  | type   | isdCode | phoneNumber | extension |
#      | 102 | Mobile | +1      | 2141112222  |           |
#      | 103 | Office | +1      | 8362223000  | 333       |
#
#    When user saves the new employee 'WITH ALL REQUIRED FIELDS'
#    Then the save 'IS SUCCESSFUL'
#
#
#
#
#  Scenario: all fields in the register form is correctly filled
#    When username, password, email, age and gender are all verified
#    Then successful registration is shown
#
#  Scenario: empty fields
#    When the client leaves any fields blank
#    Then empty fields error is shown
#
#  Scenario: user exists
#    When the client types in existing username or email
#    Then existing username or email is shown
#
#  Scenario: short password
#    When the client types in a password shorter than 8 characters
#    Then Short Error error of password is shown
#
#  Scenario: wrongly formatted email
#    When the client types in an email that is wrongly formatted
#    Then Misformatted Email error of email is shown
#
#  Scenario: user is under age
#    When the client types age smaller than 18
#    Then Under Age error is shown