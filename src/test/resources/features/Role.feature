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
      | key  | value |
      | name | test2 |
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

  Scenario: Should add sub role to a role
    Given I initialize the admin user
    And   I clean role "test_1"
    And   I clean role "test_2"
    And   I clean role "test_3"

    # Create all roles
    When I request "/roles" with method "POST" with json
      | key  | value  |
      | name | test_1 |
    Then I expect "201" as status code
    And  I set response field "id" to context "role_test_1_id"

    When I request "/roles" with method "POST" with json
      | key  | value  |
      | name | test_2 |
    Then I expect "201" as status code
    And  I set response field "id" to context "role_test_2_id"

    When I request "/roles" with method "POST" with json
      | key  | value  |
      | name | test_3 |
    Then I expect "201" as status code
    And  I set response field "id" to context "role_test_3_id"

    # Create roles hierarchy test_1 > test_2 > test_3
    When I request "/roles/[role_test_1_id]/roles" with method "POST" with body
      | value            | type |
      | [role_test_2_id] | text |
    Then I expect "201" as status code

    When I request "/roles/[role_test_2_id]/roles" with method "POST" with body
      | value            | type |
      | [role_test_3_id] | text |
    Then I expect "201" as status code

    # Should not be able to create cycle in role hierarchy
    When I request "/roles/[role_test_3_id]/roles" with method "POST" with body
      | value            | type |
      | [role_test_1_id] | text |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Entity already exists."
    And  I expect response field "code" is "208"
    And  I expect response field "field" is "association"
    And  I expect response field "value" is "NULL"
    And  I expect response field "cause" is "NULL"

    # Verify all sub roles of all roles
    When I request "/roles/[role_test_1_id]/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    When I request "/roles/[role_test_2_id]/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "id" equals to "[role_test_1_id]"
    And  I expect one resource contains "name" equals to "test_1"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"

    When I request "/roles/[role_test_3_id]/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "2"
    And  I expect one resource contains "id" equals to "[role_test_1_id]"
    And  I expect one resource contains "id" equals to "[role_test_2_id]"
    And  I expect one resource contains "name" equals to "test_1"
    And  I expect one resource contains "name" equals to "test_2"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"
    And  I expect one resource contains "isDirect" equals to "false" as "boolean"

    # Associate role 3 to user and verify that we get all roles related to user
    When I request "/roles/[role_test_3_id]/users" with method "POST" with body
      | value | type |
      | admin | text |
    Then I expect "201" as status code

    When I request "/users/admin/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "4"
    And  I expect one resource contains "id" equals to "1"
    And  I expect one resource contains "id" equals to "[role_test_1_id]"
    And  I expect one resource contains "id" equals to "[role_test_2_id]"
    And  I expect one resource contains "id" equals to "[role_test_3_id]"

    # Verify if delete link role test_2 and test_1, test_3 is not linked to test_1
    When I request "/roles/[role_test_1_id]/roles/[role_test_2_id]" with method "DELETE"
    Then I expect "204" as status code

    When I request "/roles/[role_test_3_id]/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "id" equals to "[role_test_2_id]"
    And  I expect one resource contains "name" equals to "test_2"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"

  Scenario: Should add group to a role
    Given I initialize the admin user
    And   I clean role "role"
    And   I clean group "group"

    # Create all role
    When I request "/roles" with method "POST" with json
      | key  | value |
      | name | role  |
    Then I expect "201" as status code
    And  I set response field "id" to context "role_id"

    # Create all group
    When I request "/groups" with method "POST" with json
      | key  | value |
      | name | group |
    Then I expect "201" as status code
    And  I set response field "id" to context "group_id"

    # Role shouldn't have group
    When I request "/roles/[role_id]/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Group shouldn't have role
    When I request "/groups/[group_id]/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Associate group and role
    When I request "/roles/[role_id]/groups" with method "POST" with body
      | value      | type |
      | [group_id] | text |
    Then I expect "201" as status code

    # Verify all groups of role
    When I request "/roles/[role_id]/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "id" equals to "[group_id]"
    And  I expect one resource contains "name" equals to "group"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"

    # Verify all roles of group
    When I request "/groups/[group_id]/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "id" equals to "[role_id]"
    And  I expect one resource contains "name" equals to "role"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"

    # Dissociate group and role
    When I request "/roles/[role_id]/groups/[group_id]" with method "DELETE"
    Then I expect "204" as status code

    # Role shouldn't have group
    When I request "/roles/[role_id]/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Group shouldn't have role
    When I request "/groups/[group_id]/roles" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"
