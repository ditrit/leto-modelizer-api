Feature: AI secret feature

  Scenario: Should return 404 on unknown uuid
    Given I initialize the admin user
    And I clean AI secret "SONAR_TOKEN"

    When I request "/ai/secrets" with method "POST" with json
      | key   | value              |
      | key   | SONAR_TOKEN        |
      | value | auietauieuaresuiae |
    Then I expect "201" as status code
    And  I set response field "id" to context "secret_id"

    When I request "/ai/secrets/[secret_id]" with method "DELETE"
    Then I expect "204" as status code

    When I request "/ai/secrets/[secret_id]" with method "GET"
    Then I expect "404" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Entity not found."
    And  I expect response field "code" is "204"
    And  I expect response field "field" is "id"
    And  I expect response field "value" is "[secret_id]"
    And  I expect response field "cause" is "NULL"

  Scenario Outline: Should return 400 on invalid key "<key>"
    Given I initialize the admin user

    When I request "/ai/secrets" with method "POST" with json
      | key   | value              |
      | key   |                    |
      | value | auietauieuaresuiae |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Field value is empty."
    And  I expect response field "code" is "201"
    And  I expect response field "field" is "key"
    And  I expect response field "value" is "NULL"
    And  I expect response field "cause" is "NULL"
    Examples:
      | key      |
      |          |
      | invalid= |

  Scenario: Should return 400 on invalid value
    Given I initialize the admin user
    And   I clean AI secret "SONAR_TOKEN"

    When I request "/ai/secrets" with method "POST" with json
      | key   | value       |
      | key   | SONAR_TOKEN |
      | value |             |
    Then I expect "400" as status code
    And  I expect response fields length is "5"
    And  I expect response field "message" is "Field value is empty."
    And  I expect response field "code" is "201"
    And  I expect response field "field" is "value"
    And  I expect response field "value" is "NULL"
    And  I expect response field "cause" is "NULL"

    Scenario: Should return error if key already exists
      Given I initialize the admin user
      And I clean AI secret "SONAR_TOKEN"

      When I request "/ai/secrets" with method "POST" with json
        | key   | value              |
        | key   | SONAR_TOKEN        |
        | value | auietauieuaresuiae |
      Then I expect "201" as status code
      And  I set response field "id" to context "secret_id"

      When I request "/ai/secrets" with method "POST" with json
        | key   | value              |
        | key   | SONAR_TOKEN        |
        | value | auietauieuaresuiae |
      Then I expect "400" as status code
      And  I expect response fields length is "5"
      And  I expect response field "message" is "Entity already exists."
      And  I expect response field "code" is "208"
      And  I expect response field "field" is "key"
      And  I expect response field "value" is "SONAR_TOKEN"
      And  I expect response field "cause" is "NULL"

    Scenario: Should create secret and retrieve it
      Given I initialize the admin user
      And I clean AI secret "SONAR_TOKEN"

      When I request "/ai/secrets" with method "POST" with json
        | key   | value              |
        | key   | SONAR_TOKEN        |
        | value | auietauieuaresuiae |
      Then I expect "201" as status code
      And  I set response field "id" to context "secret_id"

      When I request "/ai/secrets?key=SONAR_TOKEN" with method "GET"
      Then I expect "200" as status code
      And  I expect response field "totalElements" is "1" as "number"
      And  I extract resources from response
      And  I expect one resource contains "id" equals to "[secret_id]"
      And  I expect one resource contains "key" equals to "SONAR_TOKEN"

      When I request "/ai/secrets/[secret_id]" with method "GET"
      Then I expect "200" as status code
      And  I expect response field "id" is "[secret_id]"
      And  I expect response field "key" is "SONAR_TOKEN"

      When I request "/ai/secrets/[secret_id]" with method "PUT" with json
        | key   | value       |
        | key   | SONAR_TOKEN |
        | value | test        |
      Then I expect "200" as status code

      When I request "/ai/secrets?key=SONAR_TOKEN" with method "GET"
      Then I expect "200" as status code
      And  I expect response field "totalElements" is "1" as "number"
      And  I extract resources from response
      And  I expect one resource contains "id" equals to "[secret_id]"
      And  I expect one resource contains "key" equals to "SONAR_TOKEN"

      When I request "/ai/secrets/[secret_id]" with method "GET"
      Then I expect "200" as status code
      And  I expect response field "id" is "[secret_id]"
      And  I expect response field "key" is "SONAR_TOKEN"