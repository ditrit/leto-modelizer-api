Feature: Simple authentication

  Scenario: Register and login a user
    When I request "/api/purge/_User" with method "DELETE" with masterKey
    Then I expect 200 as status code

    When I request "/api/users" with method "POST" and body with masterKey
      | key       | value       |
      | username  | myUsername  |
      | firstname | myFirstname |
      | password  | myPassword  |
    Then I expect 201 as status code

    When I request "/api/login" with method "POST" and body
      | key      | value      |
      | username | myUsername |
      | password | myPassword |
    Then I expect 200 as status code

  Scenario: Login with a non-valid user
    When I request "/api/login" with method "POST" and body
      | key      | value       |
      | username | notUsername |
      | password | notPassword |
    Then I expect 404 as status code
    And  I expect body field "error" is "Invalid username/password."

  Scenario: Get valid authentication url
    When I request "/authenticationUrl"
    Then I expect 200 as status code
    And  I expect body is "https://github.com/login/oauth/authorize?client_id=MY_CLIENT_ID"
