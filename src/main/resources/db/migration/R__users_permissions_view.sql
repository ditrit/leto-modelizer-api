-- Ensure repeatable migration is really repeated on every start
-- ${flyway:timestamp}

DROP VIEW IF EXISTS users_permissions_view;

CREATE VIEW users_permissions_view AS
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
    encode(digest(concat(all_access_controls.usr_id::text, permissions.per_id::text), 'sha256'), 'hex') AS "usp_id",
    all_access_controls.usr_id,
    permissions.per_id,
    permissions.entity::text,
    permissions.action::text,
    permissions.lib_id,
    permissions.insert_date,
    permissions.update_date
FROM
    all_access_controls
JOIN
    permissions_access_controls
ON
    permissions_access_controls.aco_id = all_access_controls.aco_id
LEFT OUTER JOIN
    permissions
on
    permissions_access_controls.per_id = permissions.per_id;

COMMENT ON VIEW users_permissions_view IS 'This view provides a consolidated look at user permissions across the system. It combines information from various tables to present a clear picture of which permissions are assigned to which users. The view employs a recursive CTE (Common Table Expression) to traverse the hierarchical structure of access controls and match them with corresponding permissions. The final output includes unique combinations of user IDs, permission details, and associated library IDs.';
