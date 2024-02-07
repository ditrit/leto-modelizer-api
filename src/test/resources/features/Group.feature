Feature: group feature

  Scenario: Should return 200 on a valid group creation
    Given I initialize the admin user
    And   I clean group "test"

    When I request "/groups" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I expect response fields length is "2"
    And  I expect response field "name" is "test"
    And  I expect response field "id" is "NOT_NULL"

  Scenario: Should return 400 on a duplicated group creation
    Given I initialize the admin user
    And   I clean group "test"

    When I request "/groups" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code

    When  I request "/groups" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "constraint"
    And  I expect response field "value" is "access_controls_name_type_key"
    And  I expect response field "cause" is "NOT_NULL"

  Scenario: Should return 200 on a valid group update
    Given I initialize the admin user
    And   I clean group "test"
    And   I clean group "test2"

    When I request "/groups" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I set response field "id" to context "group_id"

    When I request "/groups/[group_id]" with method "PUT" with json
      | key  | value |
      | name | test2 |
    Then I expect "200" as status code

    When I request "/groups/[group_id]" with method "GET"
    Then I expect "200" as status code
    And  I expect response fields length is "2"
    And  I expect response field "name" is "test2"
    And  I expect response field "id" is "NOT_NULL"

  Scenario: Should return 400 on a duplicated group update
    Given I initialize the admin user
    And   I clean group "test"

    When I request "/groups" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I set response field "id" to context "group_id"

    When I request "/groups/[group_id]" with method "PUT" with json
      | key  | value              |
      | name | Super administrator|
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "constraint"
    And  I expect response field "value" is "access_controls_name_type_key"
    And  I expect response field "cause" is "NOT_NULL"

  Scenario: Add group to user and delete association
    Given I initialize the admin user
    And   I clean group "test"

    # Create group test
    When I request "/groups" with method "POST" with json
      | key  | value |
      | name | test  |
    Then I expect "201" as status code
    And  I set response field "id" to context "group_id"

    # Check group test has no associated users
    When I request "/groups/[group_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Check current user has only one group.
    When I request "/users/me/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Associate current user to group test
    When I request "/groups/[group_id]/users" with method "POST" with body
      | value | type |
      | admin | text |
    Then I expect "201" as status code

    # Check group test has one associated user
    When I request "/groups/[group_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "login" equals to "admin"

    # Check current user has two groups.
    When I request "/users/me/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "name" equals to "test"

    # Dissociate current user of group test
    When I request "/groups/[group_id]/users/admin" with method "DELETE"
    Then I expect "204" as status code

    # Check group test has no users
    When I request "/groups/[group_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Check current user has only one group.
    When I request "/users/me/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

  Scenario: Should add sub group to a group
    Given I initialize the admin user
    And   I clean group "test_1"
    And   I clean group "test_2"
    And   I clean group "test_3"

    # Create all groups
    When I request "/groups" with method "POST" with json
      | key  | value  |
      | name | test_1 |
    Then I expect "201" as status code
    And  I set response field "id" to context "group_test_1_id"

    When I request "/groups" with method "POST" with json
      | key  | value  |
      | name | test_2 |
    Then I expect "201" as status code
    And  I set response field "id" to context "group_test_2_id"

    When I request "/groups" with method "POST" with json
      | key  | value  |
      | name | test_3 |
    Then I expect "201" as status code
    And  I set response field "id" to context "group_test_3_id"

    # Create groups hierarchy test_1 > test_2 > test_3
    When I request "/groups/[group_test_1_id]/groups" with method "POST" with body
      | value             | type |
      | [group_test_2_id] | text |
    Then I expect "201" as status code

    When I request "/groups/[group_test_2_id]/groups" with method "POST" with body
      | value             | type |
      | [group_test_3_id] | text |
    Then I expect "201" as status code

    # Should not be able to create cycle in group hierarchy
    When I request "/groups/[group_test_3_id]/groups" with method "POST" with body
      | value             | type |
      | [group_test_1_id] | text |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Entity already exists."
    And  I expect response field "code" is "208"
    And  I expect response field "field" is "association"
    And  I expect response field "value" is "NULL"
    And  I expect response field "cause" is "NULL"

    # Verify all sub groups of all groups
    When I request "/groups/[group_test_1_id]/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    When I request "/groups/[group_test_2_id]/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "id" equals to "[group_test_1_id]"
    And  I expect one resource contains "name" equals to "test_1"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"

    When I request "/groups/[group_test_3_id]/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "2"
    And  I expect one resource contains "id" equals to "[group_test_1_id]"
    And  I expect one resource contains "id" equals to "[group_test_2_id]"
    And  I expect one resource contains "name" equals to "test_1"
    And  I expect one resource contains "name" equals to "test_2"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"
    And  I expect one resource contains "isDirect" equals to "false" as "boolean"

    # Associate group 3 to user and verify that we get all groups related to user
    When I request "/groups/[group_test_3_id]/users" with method "POST" with body
      | value | type |
      | admin | text |
    Then I expect "201" as status code

    When I request "/users/admin/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "3"
    And  I expect one resource contains "id" equals to "[group_test_1_id]"
    And  I expect one resource contains "id" equals to "[group_test_2_id]"
    And  I expect one resource contains "id" equals to "[group_test_3_id]"

    # Verify if delete link group test_2 and test_1, test_3 is not linked to test_1
    When I request "/groups/[group_test_1_id]/groups/[group_test_2_id]" with method "DELETE"
    Then I expect "204" as status code

    When I request "/groups/[group_test_3_id]/groups" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "id" equals to "[group_test_2_id]"
    And  I expect one resource contains "name" equals to "test_2"
    And  I expect one resource contains "isDirect" equals to "true" as "boolean"
