Feature: Role management

  Scenario: Check admin default user is initialized
    When I request "/api/classes/_Role" with method "GET" with query parameter with masterKey
      | key   | value            |
      | where | {"name":"admin"} |
    Then I expect length of array body field "results" is 1

  Scenario: Set and unset a Role to a User
    Given I purge role "moderator"
    And   I purge all users

    When I request "/api/users" with method "POST" and body with masterKey
      | key       | value       |
      | username  | myUsername  |
      | firstname | myFirstname |
      | password  | myPassword  |
    Then I expect 201 as status code
    And  I set body field "objectId" to context field "user_id"

    When I request "/api/classes/_Role" with method "POST" with body as json with masterKey
    """
    {"name" : "moderator", "ACL": {"*": {"read":true} }, "users": { "__op": "AddRelation", "objects": [{ "__type": "Pointer", "className": "_User", "objectId": "{{user_id}}" }] } }
    """
    Then I expect 201 as status code
    And  I set body field "objectId" to context field "role_id"

    When I request "/api/classes/_User" with method "GET" with query parameter with masterKey
      | key   | value                                                                                                                    |
      | where | { "$relatedTo": { "object": { "__type": "Relation", "className": "_Role", "objectId": "{{role_id}}" }, "key": "users"} } |
    Then I expect 200 as status code
    And  I expect length of array body field "results" is 1

    When I request "/api/classes/_Role/{{role_id}}" with method "PUT" with body as json with masterKey
    """
    {"users": { "__op": "RemoveRelation", "objects": [{ "__type": "Relation", "className": "_User", "objectId": "{{user_id}}" }] } }
    """
    Then I expect 200 as status code

    When I request "/api/classes/_User" with method "GET" with query parameter with masterKey
      | key   | value                                                                                                                     |
      | where | { "$relatedTo": { "object": { "__type": "Relation", "className": "_Role", "objectId": "{{role_id}}" }, "key": "users" } } |
    Then I expect 200 as status code
    And  I expect length of array body field "results" is 0

    When I request "/api/classes/_Role/{{role_id}}" with method "Delete" with masterKey
    Then I expect 200 as status code

    When I request "/api/classes/_User/{{user_id}}" with method "Delete" with masterKey
    Then I expect 200 as status code

  Scenario: Set and unset a Role to a Role
    Given I purge role "moderator"
    And   I purge role "member"

    When I request "/api/classes/_Role" with method "POST" with body as json with masterKey
    """
    {"name" : "member", "ACL": {"*": {"read":true} } }
    """
    Then I expect 201 as status code
    And  I set body field "objectId" to context field "member_role_id"

    When I request "/api/classes/_Role" with method "POST" with body as json with masterKey
    """
    {"name" : "moderator", "ACL": {"*": {"read":true} }, "roles": { "__op": "AddRelation", "objects": [{ "__type": "Pointer", "className": "_Role", "objectId": "{{member_role_id}}" }] } }
    """
    Then I expect 201 as status code
    And  I set body field "objectId" to context field "moderator_role_id"

    When I request "/api/classes/_Role" with method "GET" with query parameter with masterKey
      | key   | value                                                                                                               |
      | where | {"$relatedTo":{"object":{"__type":"Pointer","className":"_Role","objectId":"{{moderator_role_id}}"},"key":"roles"}} |
    Then I expect 200 as status code
    And  I expect length of array body field "results" is 1

    When I request "/api/classes/_Role/{{moderator_role_id}}" with method "PUT" with body as json with masterKey
    """
    {"roles": { "__op": "RemoveRelation", "objects": [{ "__type": "Pointer", "className": "_Role", "objectId": "{{member_role_id}}" }] } }
    """
    Then I expect 200 as status code

    When I request "/api/classes/_Role" with method "GET" with query parameter with masterKey
      | key   | value                                                                                                               |
      | where | {"$relatedTo":{"object":{"__type":"Pointer","className":"_Role","objectId":"{{moderator_role_id}}"},"key":"roles"}} |
    Then I expect 200 as status code
    And  I expect length of array body field "results" is 0

    When I request "/api/classes/_Role/{{member_role_id}}" with method "Delete" with masterKey
    Then I expect 200 as status code

    When I request "/api/classes/_Role/{{moderator_role_id}}" with method "Delete" with masterKey
    Then I expect 200 as status code
