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
