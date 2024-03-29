-- Ensure repeatable migration is really repeated on every start
-- ${flyway:timestamp}

DROP VIEW IF EXISTS users_libraries_view;

CREATE VIEW users_libraries_view AS
WITH RECURSIVE all_access_controls AS (
    SELECT
        aco_id,
        usr_id
    FROM
        users_access_controls
    UNION
    SELECT
        parent,
        usr_id
    FROM
        access_controls_tree
    INNER JOIN
        all_access_controls
    ON
        all_access_controls.aco_id = access_controls_tree.current
)
SELECT DISTINCT
    concat(all_access_controls.usr_id::text, libraries.lib_id::text) AS "uli_id",
    all_access_controls.usr_id,
    libraries.*
FROM
    all_access_controls
JOIN
    access_controls_permissions
ON
    access_controls_permissions.aco_id = all_access_controls.aco_id
LEFT OUTER JOIN
    permissions
ON
    access_controls_permissions.per_id = permissions.per_id
LEFT OUTER JOIN
    libraries
ON
    libraries.lib_id = permissions.lib_id
AND permissions.action = 'ACCESS'
WHERE
    permissions.lib_id IS NOT NULL;

-- Unsupported by cockroachdb
-- COMMENT ON VIEW users_libraries_view IS 'The users_libraries_view is a database view designed to provide a comprehensive overview of user access to libraries. It leverages a recursive common table expression (CTE) to traverse access control hierarchies and aggregates this information to determine which libraries each user can access.';
