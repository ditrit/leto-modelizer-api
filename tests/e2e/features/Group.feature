Feature: Group of users management

  Scenario: Create two groups with same name
    When I request "/api/purge/Group" with method "DELETE" with masterKey
    Then I expect 200 as status code

    When I request "/api/classes/Group" with method "POST" and body with masterKey
      | key  | value       |
      | name | myGroupName |
    Then I expect 201 as status code

    When I request "/api/classes/Group" with method "POST" and body with masterKey
      | key  | value       |
      | name | myGroupName |
    Then I expect 400 as status code
