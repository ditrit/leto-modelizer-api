Feature: Library management
  Scenario: Create two Libraries with the same URL:
    Given I purge all libraries

    When I request "/api/classes/Library" with method "POST" and body with masterKey
      | key      | value                            |
      | roleName | my_lib_role                      |
      | url      | http://templates/lib1/index.json |
    Then I expect 201 as status code
    And I set body field "objectId" to context field "lib_id"

    When I request "/api/classes/Library" with method "POST" and body with masterKey
      | key      | value                            |
      | roleName | my_lib_role2                     |
      | url      | http://templates/lib1/index.json |
    Then I expect 400 as status code
    And I expect body field "error" is "Library with this url already exists"

    When I request "/api/classes/Library/{{lib_id}}" with method "DELETE" with masterKey
    Then I expect 200 as status code

  Scenario: Create two Libraries with the same roleName:
    Given I purge all libraries

    When I request "/api/classes/Library" with method "POST" and body with masterKey
      | key      | value                            |
      | roleName | my_lib_role                      |
      | url      | http://templates/lib1/index.json |
    Then I expect 201 as status code
    And I set body field "objectId" to context field "lib_id"

    When I request "/api/classes/Library" with method "POST" and body with masterKey
      | key      | value                            |
      | roleName | my_lib_role                      |
      | url      | http://templates/lib2/index.json |
    Then I expect 400 as status code
    And I expect body field "error" is "Library with this roleName already exists"

    When I request "/api/classes/Library/{{lib_id}}" with method "DELETE" with masterKey
    Then I expect 200 as status code

  Scenario: Create a Library with roleName in a wrong format:
    Given I purge all libraries

    When I request "/api/classes/Library" with method "POST" and body with masterKey
      | key      | value                            |
      | roleName | myLibRole                        |
      | url      | http://templates/lib2/index.json |
    Then I expect 400 as status code
    And I expect body field "error" is "roleName must contain only lower case alphanumerics and underscores"

  Scenario: Create a Library with unauthorized url:
    Given I purge all libraries

    When I request "/api/classes/Library" with method "POST" and body with masterKey
      | key      | value                                         |
      | roleName | mylibrole                                     |
      | url      | http://templates/unauthorizedlib3/index.json  |
    Then I expect 400 as status code
    And I expect body field "error" is "unauthorized domain"

  Scenario: Create and update a Library:

    When I request "/api/classes/Library" with method "POST" and body with masterKey
      | key      | value                            |
      | roleName | my_lib_role                      |
      | url      | http://templates/lib1/index.json |
    Then I expect 201 as status code
    And I set body field "objectId" to context field "lib_id"

    When I request "/api/classes/Template" with method "GET" with query parameter with masterKey
      | key   | value                                                                                    |
      | where | { "library": { "__type": "Pointer", "className": "Library", "objectId": "{{lib_id}}" } } |
    Then I expect 200 as status code
    And  I expect length of array body field "results" is 1

    When I request "/api/classes/Library/{{lib_id}}" with method "PUT" and body with masterKey
      | key   | value                            |
      | url   | http://templates/lib2/index.json |
    Then I expect 200 as status code

    When I request "/api/classes/Template" with method "GET" with query parameter with masterKey
      | key   | value                                                                                    |
      | where | { "library": { "__type": "Pointer", "className": "Library", "objectId": "{{lib_id}}" } } |
    Then I expect 200 as status code
    And  I expect length of array body field "results" is 4

    When I request "/api/classes/Library/{{lib_id}}" with method "DELETE" with masterKey
    Then I expect 200 as status code
