Feature: Role feature

  Scenario: Should return 200 on a valid role creation
    Given I initialize the admin user
    And   I clean role "test"

    When I request "/roles" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I expect response fields length is "2"
    And  I expect response field "name" is "test"
    And  I expect response field "id" is "NOT_NULL"

  Scenario: Should return 400 on a duplicated role creation
    Given I initialize the admin user
    When  I request "/roles" with method "POST" with json
      | key  | value               |
      | name | Super administrator |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "constraint"
    And  I expect response field "value" is "access_controls_name_type_key"
    And  I expect response field "cause" is "NOT_NULL"

  Scenario: Should return 200 on a valid role update
    Given I initialize the admin user
    And   I clean role "test"

    When I request "/roles" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I set response field "id" to context "role_id"

    When I request "/roles/[role_id]" with method "PUT" with json
      | key  | value  |
      | name | test2  |
    Then I expect "200" as status code

    When I request "/roles/[role_id]" with method "GET"
    Then I expect "200" as status code
    And  I expect response fields length is "2"
    And  I expect response field "name" is "test2"
    And  I expect response field "id" is "NOT_NULL"

  Scenario: Should return 400 on a duplicated role update
    Given I initialize the admin user
    And   I clean role "test"

    When I request "/roles" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I set response field "id" to context "role_id"

    When I request "/roles/[role_id]" with method "PUT" with json
      | key  | value               |
      | name | Super administrator |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "constraint"
    And  I expect response field "value" is "access_controls_name_type_key"
    And  I expect response field "cause" is "NOT_NULL"

  Scenario: Should return all user associate to a specific role
    Given I initialize the admin user

    When I request "/roles/1/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "login" equals to "admin"

  Scenario: Add role to user and delete association
    Given I initialize the admin user
    And   I clean role "test"

    # Create role test
    When I request "/roles" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I set response field "id" to context "role_id"

    # Check role test has no associated users
    When I request "/roles/[role_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Check current user has only one role.
    When I request "/users/me/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "name" equals to "Super administrator"

    # Associate current user to role test
    When I request "/roles/[role_id]/users" with method "POST" with body
      | value | type |
      | admin | text |
    Then I expect "201" as status code

    # Check role test has one associated user
    When I request "/roles/[role_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "login" equals to "admin"

    # Check current user has two roles.
    When I request "/users/me/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "2"
    And  I expect one resource contains "name" equals to "test"
    And  I expect one resource contains "name" equals to "Super administrator"

    # Dissociate current user of role test
    When I request "/roles/[role_id]/users/admin" with method "DELETE"
    Then I expect "204" as status code

    # Check role test has no users
    When I request "/roles/[role_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Check current user has only one role.
    When I request "/users/me/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "name" equals to "Super administrator"
