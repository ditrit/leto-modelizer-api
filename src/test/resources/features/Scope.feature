Feature: Scope feature

  Scenario: Should return 200 on a valid scope creation
    Given I initialize the admin user
    And   I clean scope "TEST"

    When I request "/scopes" with method "POST" with json
      | key  | value |
      | name | TEST  |
    Then I expect "201" as status code
    And  I expect response fields length is "2"
    And  I expect response field "name" is "TEST"
    And  I expect response field "id" is "NOT_NULL"

  Scenario: Should return 400 on a duplicated scope creation
    Given I initialize the admin user
    And   I clean scope "TEST"

    When I request "/scopes" with method "POST" with json
      | key  | value |
      | name | TEST  |
    Then I expect "201" as status code

    When I request "/scopes" with method "POST" with json
      | key  | value |
      | name | TEST  |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "constraint"
    And  I expect response field "value" is "access_controls_name_type_key"
    And  I expect response field "cause" is "NOT_NULL"

  Scenario: Should return 200 on a valid scope update
    Given I initialize the admin user
    And   I clean scope "TEST"
    And   I clean scope "TEST2"

    When I request "/scopes" with method "POST" with json
      | key  | value |
      | name | TEST  |
    Then I expect "201" as status code
    And  I set response field "id" to context "scope_id"

    When I request "/scopes/[scope_id]" with method "PUT" with json
      | key  | value |
      | name | TEST2 |
    Then I expect "200" as status code

    When I request "/scopes/[scope_id]" with method "GET"
    Then I expect "200" as status code
    And  I expect response fields length is "2"
    And  I expect response field "name" is "TEST2"
    And  I expect response field "id" is "NOT_NULL"

  Scenario: Should return 400 on a duplicated scope update
    Given I initialize the admin user
    And   I clean scope "TEST"
    And   I clean scope "TEST2"

    When I request "/scopes" with method "POST" with json
      | key  | value |
      | name | TEST  |
    Then I expect "201" as status code
    And  I set response field "id" to context "scope_id"

    When I request "/scopes" with method "POST" with json
      | key  | value |
      | name | TEST2 |
    Then I expect "201" as status code

    When I request "/scopes/[scope_id]" with method "PUT" with json
      | key  | value |
      | name | TEST2 |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "constraint"
    And  I expect response field "value" is "access_controls_name_type_key"
    And  I expect response field "cause" is "NOT_NULL"

  Scenario: Add scope to user and delete association
    Given I initialize the admin user
    And   I clean scope "TEST"

    # Create scope test
    When I request "/scopes" with method "POST" with json
      | key  | value |
      | name | TEST  |
    Then I expect "201" as status code
    And  I set response field "id" to context "scope_id"

    # Check scope test has no associated users
    When I request "/scopes/[scope_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Check current user has no scope
    When I request "/users/me/scopes" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Associate current user to scope test
    When I request "/scopes/[scope_id]/users" with method "POST" with body
      | value | type |
      | admin | text |
    Then I expect "201" as status code

    # Check scope test has one associated user
    When I request "/scopes/[scope_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "login" equals to "admin"

    # Check current user has one scope
    When I request "/users/me/scopes" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "1"
    And  I expect one resource contains "name" equals to "TEST"

    # Dissociate current user of scope test
    When I request "/scopes/[scope_id]/users/admin" with method "DELETE"
    Then I expect "204" as status code

    # Check scope test has no users
    When I request "/scopes/[scope_id]/users" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"

    # Check current user has no scope
    When I request "/users/me/scopes" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect response resources length is "0"
