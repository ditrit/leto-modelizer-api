Feature: Library feature

  Scenario: Should return 400 on invalid library url
    Given I initialize the admin user

    When I request "/libraries" with method "POST" with json
      | key | value                  |
      | url | http://[LIBRARY_HOST]/ |
    Then I expect "400" as status code
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "url"
    And  I expect response field "value" is "http://[LIBRARY_HOST]/"

    When I request "/libraries/validate" with method "POST" with body
      | value                  | type |
      | http://[LIBRARY_HOST]/ | text |
    Then I expect "400" as status code
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "url"
    And  I expect response field "value" is "http://[LIBRARY_HOST]/"

  Scenario: Should return 400 on unauthorized library url
    Given I initialize the admin user

    When I request "/libraries" with method "POST" with json
      | key | value                         |
      | url | http://invalidhost/index.json |
    Then I expect "400" as status code
    And  I expect response field "message" is "Url of library is unauthorized."
    And  I expect response field "code" is "209"
    And  I expect response field "field" is "url"
    And  I expect response field "value" is "http://invalidhost/index.json"

    When I request "/libraries/validate" with method "POST" with body
      | value                         | type |
      | http://invalidhost/index.json | text |
    Then I expect "400" as status code
    And  I expect response field "message" is "Url of library is unauthorized."
    And  I expect response field "code" is "209"
    And  I expect response field "field" is "url"
    And  I expect response field "value" is "http://invalidhost/index.json"

  Scenario: Should return 400 on unknown library url
    Given I initialize the admin user
    And   I clean role "TEST"

    When I request "/libraries" with method "POST" with json
      | key  | value                                            |
      | role | TEST                                             |
      | url  | http://[LIBRARY_HOST]/invalid/unknown/index.json |
    Then I expect "400" as status code
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "url"
    And  I expect response field "value" is "http://[LIBRARY_HOST]/invalid/unknown/index.json"

    When I request "/libraries/validate" with method "POST" with body
      | value                                            | type |
      | http://[LIBRARY_HOST]/invalid/unknown/index.json | text |
    Then I expect "400" as status code
    And  I expect response field "message" is "Wrong field value."
    And  I expect response field "code" is "206"
    And  I expect response field "field" is "url"
    And  I expect response field "value" is "http://[LIBRARY_HOST]/invalid/unknown/index.json"

  Scenario: Should return 400 on invalid library json
    Given I initialize the admin user
    And   I clean role "TEST"

    When I request "/libraries" with method "POST" with json
      | key  | value                                           |
      | role | TEST                                            |
      | url  | http://[LIBRARY_HOST]/invalid/simple/index.json |
    Then I expect "400" as status code
    And  I expect response field "message" is "Index.json of library is invalid."
    And  I expect response field "code" is "210"
    And  I expect response field "field" is "REQUIRED"
    And  I expect response field "value" is "required properties are missing: name"

    When I request "/libraries/validate" with method "POST" with body
      | value                                           | type |
      | http://[LIBRARY_HOST]/invalid/simple/index.json | text |
    Then I expect "400" as status code
    And  I expect response field "message" is "Index.json of library is invalid."
    And  I expect response field "code" is "210"
    And  I expect response field "field" is "REQUIRED"
    And  I expect response field "value" is "required properties are missing: name"

  Scenario: Should return 400 on already exists library
    Given I initialize the admin user
    And   I clean library "http://[LIBRARY_HOST]/valid/simple/"
    And   I clean role "TEST1"
    And   I clean role "TEST2"

    When I request "/libraries" with method "POST" with json
      | key  | value                                         |
      | role | TEST1                                         |
      | url  | http://[LIBRARY_HOST]/valid/simple/index.json |
    Then I expect "201" as status code

    When I request "/libraries" with method "POST" with json
      | key  | value                                         |
      | role | TEST2                                         |
      | url  | http://[LIBRARY_HOST]/valid/simple/index.json |
    Then I expect "400" as status code
    And  I expect response field "message" is "Entity already exists."
    And  I expect response field "code" is "208"
    And  I expect response field "field" is "url"
    And  I expect response field "value" is "http://[LIBRARY_HOST]/valid/simple/"

  Scenario: Should return 200 on a valid library creation
    Given I initialize the admin user
    And   I clean library "http://[LIBRARY_HOST]/valid/simple/"
    And   I clean role "TEST"

    When I request "/libraries/validate" with method "POST" with body
      | value                                         | type |
      | http://[LIBRARY_HOST]/valid/simple/index.json | text |
    Then I expect "204" as status code

    # Library creation
    When I request "/libraries" with method "POST" with json
      | key  | value                                         |
      | role | TEST                                          |
      | url  | http://[LIBRARY_HOST]/valid/simple/index.json |
    Then I expect "201" as status code
    And  I set response field "id" to context "libraryId"

    # Check if library is created
    When I request "/libraries/[libraryId]" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "id" is "[libraryId]"

    # Verify there is only one library registered
    When I request "/libraries" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "totalElements" is "1" as "number"

    # Check if library templates are created
    When I request "/libraries/templates" with method "GET"
    Then I expect "206" as status code
    And  I expect response field "totalElements" is "33" as "number"

    # Delete library
    When I request "/libraries/[libraryId]" with method "DELETE"
    Then I expect "204" as status code

    # Check if library is deleted
    When I request "/libraries/[libraryId]" with method "GET"
    Then I expect "404" as status code

  Scenario: Should return 200 on a valid library and role creation
    Given I initialize the admin user
    And   I clean role "ROLE_OF_LIB_1"
    And   I clean library "http://[LIBRARY_HOST]/valid/simple/"

    # Library creation
    When I request "/libraries" with method "POST" with json
      | key  | value                                    |
      | url  | http://libraries/valid/simple/index.json |
      | role | ROLE_OF_LIB_1                            |
    Then I expect "201" as status code
    And  I set response field "id" to context "libraryId"

    When I request "/roles?name=ROLE_OF_LIB_1" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "totalElements" is "1" as "number"
    And  I extract first resource from response
    And  I set response field "id" to context "roleId"

    When I request "/roles/[roleId]/permissions" with method "GET"
    Then I expect "200" as status code
    And  I expect response field "totalElements" is "3" as "number"

    And I clean role "ROLE_OF_LIB_1"
    And I clean library "http://[LIBRARY_HOST]/valid/simple/"
