Feature: Simple authentication

  Scenario: Register and login a user
    When I request "/purge/_User" with method "DELETE" with masterKey
    Then I expect 200 as status code

    When I request "/users" with method "POST" and body with masterKey
    | key       | value       |
    | username  | myUsername  |
    | firstname | myFirstname |
    | password  | myPassword  |
    Then I expect 201 as status code

    When I request "/login" with method "POST" and body
    | key      | value      |
    | username | myUsername |
    | password | myPassword |
    Then I expect 200 as status code

  Scenario: Login with a non-valid user
    When I request "/login" with method "POST" and body
    | key      | value       |
    | username | notUsername |
    | password | notPassword |
    Then I expect 404 as status code
    And  I expect body field "error" is "Invalid username/password."
