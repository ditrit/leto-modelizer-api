Feature: Common server feature

  Scenario: Verify server is up
    When I request "/api/health"
    Then I expect 200 as status code
    And  I expect body contains 1 attribute
    And  I expect body field "status" is "ok"
