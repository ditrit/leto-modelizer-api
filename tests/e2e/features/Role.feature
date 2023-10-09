Feature: Role management

  Scenario: Check admin default user is initialized
    When I request "/api/roles" with method "GET" with body as json in url encoded mode and masterKey
    """
    where={"name":"admin"}
    """
    Then I expect length of array body field "results" is 1

  Scenario: Set and unset a Role to a User
    When I request "/api/purge/_User" with method "DELETE" with masterKey
    Then I expect 200 as status code

    When I request "/api/users" with method "POST" and body with masterKey
    | key       | value       |
    | username  | myUsername  |
    | firstname | myFirstname |
    | password  | myPassword  |
    Then I expect 201 as status code
    And I set body field "objectId" to context field "user_id"

    When I request "/api/purge/_Role" with method "DELETE" with masterKey
    Then I expect 200 as status code

    When I request "/api/roles" with method "POST" with body as json and masterKey
    """
    {"name" : "moderator", "ACL": {"*": {"read":true} }, "users": { "__op": "AddRelation", "objects": [{ "__type": "Pointer", "className": "_User", "objectId": "{{user_id}}" }] } }
    """
    Then I expect 201 as status code
    And I set body field "objectId" to context field "role_id"

    When I request "/api/users" with method "GET" with body as json in url encoded mode and masterKey
    """
    where={"$relatedTo":{"object":{"__type":"Pointer","className":"Role","objectId":"{{role_id}}"},"key":"users"} }
    """
    Then I expect 200 as status code

    When I request "/api/roles/{{role_id}}" with method "PUT" with body as json and masterKey
    """
    {"users": { "__op": "RemoveRelation", "objects": [{ "__type": "Pointer", "className": "_User", "objectId": "{{user_id}}" }] } }
    """
    Then I expect 200 as status code

  Scenario: Set and unset a Role to a Role
    When I request "/api/purge/_Role" with method "DELETE" with masterKey
    Then I expect 200 as status code

    When I request "/api/roles" with method "POST" with body as json and masterKey
    """
    {"name" : "member", "ACL": {"*": {"read":true} } }
    """
    Then I expect 201 as status code
    And I set body field "objectId" to context field "member_role_id"

    When I request "/api/roles" with method "POST" with body as json and masterKey
    """
    {"name" : "moderator", "ACL": {"*": {"read":true} }, "roles": { "__op": "AddRelation", "objects": [{ "__type": "Pointer", "className": "_Role", "objectId": "{{member_role_id}}" }] } }
    """
    Then I expect 201 as status code
    And I set body field "objectId" to context field "moderator_role_id"

    When I request "/api/roles/{{moderator_role_id}}" with method "PUT" with body as json and masterKey
    """
    {"users": { "__op": "RemoveRelation", "objects": [{ "__type": "Pointer", "className": "_User", "objectId": "{{member_user_id}}" }] } }
    """
    Then I expect 200 as status code
