Feature: AI configuration feature

  Scenario: Should return 400 on invalid key
    Given I initialize the admin user
    And I clean AI configuration "config"

    When I request "/ai/configurations" with method "POST" with json
      | key   | value |
      | key   |       |
      | value | value |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Field value is empty."
    And  I expect response field "code" is "201"
    And  I expect response field "field" is "key"
    And  I expect response field "value" is "NULL"
    And  I expect response field "cause" is "NULL"

  Scenario: Should return 400 on invalid value
    Given I initialize the admin user
    And I clean AI configuration "config"

    When I request "/ai/configurations" with method "POST" with json
      | key   | value  |
      | key   | config |
      | value |        |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Field value is empty."
    And  I expect response field "code" is "201"
    And  I expect response field "field" is "value"
    And  I expect response field "value" is "NULL"
    And  I expect response field "cause" is "NULL"

  Scenario: Should return 400 on duplicated key
    Given I initialize the admin user
    And I clean AI configuration "config"

    When I request "/ai/configurations" with method "POST" with json
      | key   | value  |
      | key   | config |
      | value | value  |
    Then I expect "201" as status code

    When I request "/ai/configurations" with method "POST" with json
      | key   | value  |
      | key   | config |
      | value | value  |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Entity already exists."
    And  I expect response field "code" is "208"
    And  I expect response field "field" is "key"
    And  I expect response field "value" is "config"
    And  I expect response field "cause" is "NULL"

  Scenario: Should return 400 on duplicated key with same handler
    Given I initialize the admin user
    And I clean AI configuration "config"

    When I request "/ai/configurations" with method "POST" with json
      | key     | value  |
      | handler | test   |
      | key     | config |
      | value   | value  |
    Then I expect "201" as status code

    When I request "/ai/configurations" with method "POST" with json
      | key     | value  |
      | handler | test   |
      | key     | config |
      | value   | value  |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Entity already exists."
    And  I expect response field "code" is "208"
    And  I expect response field "field" is "key"
    And  I expect response field "value" is "config"
    And  I expect response field "cause" is "NULL"

  Scenario: Should return 200 on valid creation
    Given I initialize the admin user
    And I clean AI configuration "config"

    When I request "/ai/configurations" with method "POST" with json
      | key     | value  |
      | handler | test   |
      | key     | config |
      | value   | value  |
    Then I expect "201" as status code
    And I set response field "id" to context "configuration_id"

    When I request "/ai/configurations/[configuration_id]" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "id" is "[configuration_id]"
    And  I expect response field "handler" is "test"
    And  I expect response field "key" is "config"
    And  I expect response field "value" is "value"
    And  I expect response field "updateDate" is "NOT_NULL"

  Scenario: Should return 200 with same key but different config
    Given I initialize the admin user
    And   I clean AI configuration "config"

    When I request "/ai/configurations" with method "POST" with json
      | key     | value  |
      | handler | test   |
      | key     | config |
      | value   | value  |
    Then I expect "201" as status code
    And  I set response field "id" to context "configuration1_id"

    When I request "/ai/configurations" with method "POST" with json
      | key     | value  |
      | handler |        |
      | key     | config |
      | value   | value  |
    Then I expect "201" as status code
    And  I set response field "id" to context "configuration2_id"

    When I request "/ai/configurations" with method "POST" with json
      | key     | value  |
      | handler | other  |
      | key     | config |
      | value   | value  |
    Then I expect "201" as status code
    And  I set response field "id" to context "configuration3_id"

    When I request "/ai/configurations/[configuration1_id]" with method "DELETE"
    And  I expect "204" as status code

    When I request "/ai/configurations/[configuration2_id]" with method "DELETE"
    And  I expect "204" as status code

    When I request "/ai/configurations/[configuration3_id]" with method "DELETE"
    And  I expect "204" as status code

    Scenario: should return 200 on configuration update
      Given I initialize the admin user
      And   I clean AI configuration "config"

      When I request "/ai/configurations" with method "POST" with json
        | key     | value  |
        | handler | test   |
        | key     | config |
        | value   | value  |
      Then I expect "201" as status code
      And  I set response field "id" to context "configuration_id"

      When I request "/ai/configurations/[configuration_id]" with method "GET"
      Then I expect "200" as status code
      And  I expect response field "id" is "[configuration_id]"
      And  I expect response field "handler" is "test"
      And  I expect response field "key" is "config"
      And  I expect response field "value" is "value"
      And  I expect response field "updateDate" is "NOT_NULL"

      When I request "/ai/configurations/[configuration_id]" with method "PUT" with json
        | key     | value  |
        | handler | test   |
        | key     | config |
        | value   | value2 |
      Then I expect "200" as status code

      When I request "/ai/configurations/[configuration_id]" with method "GET"
      Then I expect "200" as status code
      And  I expect response field "id" is "[configuration_id]"
      And  I expect response field "handler" is "test"
      And  I expect response field "key" is "config"
      And  I expect response field "value" is "value2"
      And  I expect response field "updateDate" is "NOT_NULL"

  Scenario: should return 200 on multiple configurations update
    Given I initialize the admin user
    And   I clean AI configuration "config1"
    And   I clean AI configuration "config2"

    When I request "/ai/configurations" with method "POST" with json
      | key     | value   |
      | handler | test    |
      | key     | config1 |
      | value   | value   |
    Then I expect "201" as status code
    And  I set response field "id" to context "configuration1_id"

    When I request "/ai/configurations/[configuration1_id]" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "id" is "[configuration1_id]"
    And  I expect response field "handler" is "test"
    And  I expect response field "key" is "config1"
    And  I expect response field "value" is "value"
    And  I expect response field "updateDate" is "NOT_NULL"

    When I request "/ai/configurations" with method "POST" with json
      | key     | value   |
      | handler | test    |
      | key     | config2 |
      | value   | value   |
    Then I expect "201" as status code
    And  I set response field "id" to context "configuration2_id"

    When I request "/ai/configurations/[configuration2_id]" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "id" is "[configuration2_id]"
    And  I expect response field "handler" is "test"
    And  I expect response field "key" is "config2"
    And  I expect response field "value" is "value"
    And  I expect response field "updateDate" is "NOT_NULL"