Feature: ai feature

  Scenario: Should return 201 on a valid ai diagram creation
    Given I initialize the admin user

    When I request "/ai/generate" with method "POST" with json
      | key         | value                 |
      | plugin      | terrator-plugin       |
      | type        | diagram               |
      | description | Test diagram creation |
    Then I expect "201" as status code
    And  I expect response is '[{"name":"main.tf","content":"provider \\"aws\\" {}"}]'

  Scenario: Should return 201 on a valid AI conversation creation
    Given I initialize the admin user
    And   I clean the AI conversation "TEST/TEST/@ditrit/terrator-plugin"

    When I request "/ai/conversations" with method "POST" with json
      | key      | value                   | type   |
      | project  | TEST                    | string |
      | diagram  | TEST                    | string |
      | plugin   | @ditrit/terrator-plugin | string |
      | checksum | ABCDEF                  | string |
      | files    | []                      | array  |
    Then I expect "201" as status code
    And  I expect response fields length is "5"
    And  I expect response field "key" is "TEST/TEST/@ditrit/terrator-plugin"
    And  I expect response field "id" is "NOT_NULL"
    And  I expect response field "checksum" is "ABCDEF"
    And  I expect response field "size" is "0"
    And  I expect response field "updateDate" is "NOT_NULL"
    And  I set response field "id" to context "conversation_id"

    When I request "/ai/conversations/[conversation_id]" with method "GET"
    Then I expect "200" as status code
    And  I expect response fields length is "5"
    And  I expect response field "key" is "TEST/TEST/@ditrit/terrator-plugin"
    And  I expect response field "id" is "NOT_NULL"
    And  I expect response field "size" is "0"
    And  I expect response field "checksum" is "ABCDEF"
    And  I expect response field "updateDate" is "NOT_NULL"

    When I request "/users/admin/ai/conversations" with method "GET"
    Then I expect "200" as status code
    And  I extract resources from response
    And  I expect one resource contains "id" equals to "[conversation_id]"

    When I request "/ai/conversations/[conversation_id]/messages" with method "POST" with body
      | body | type   |
      | test | String |
    Then I expect "201" as status code
    And  I expect response fields length is "5"
    And  I expect response field "id" is "NOT_NULL"
    And  I expect response field "aiConversation" is "[conversation_id]"
    And  I expect response field "isUser" is "false"
    And  I expect response field "message" is "H4sIAAAAAAAA//P3BgAt2TbXAgAAAA=="
    And  I expect response field "updateDate" is "NOT_NULL"

    When I request "/ai/conversations/[conversation_id]" with method "DELETE"
    Then I expect "204" as status code

    When I request "/ai/conversations/[conversation_id]" with method "GET"
    Then I expect "404" as status code
