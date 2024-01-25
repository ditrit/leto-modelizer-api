Feature: Application feature

  Scenario: Check if application is up
    Given I initialize the admin user
    When I request "/actuator/health" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "status" is "UP"
