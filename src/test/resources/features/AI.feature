Feature: ai feature

  Scenario: Should return 201 on a valid ai diagram creation
    Given I initialize the admin user

    When I request "/ai" with method "POST" with json
      | key         | value                 |
      | plugin      | terrator-plugin       |
      | type        | diagram               |
      | description | Test diagram creation |
    Then I expect "201" as status code
    And  I expect response is '[{"name":"main.tf","content":"provider \\"aws\\" {}"}]'
