-- Ensure repeatable migration is really repeated on every start
-- ${flyway:timestamp}

DROP VIEW IF EXISTS access_controls_permissions_view;

CREATE VIEW access_controls_permissions_view AS
WITH RECURSIVE all_access_controls AS (
    SELECT
        aco_id,
        aco_id as "parent",
        FALSE as "direct"
    FROM
        access_controls
    UNION
    SELECT
        all_access_controls.aco_id,
        access_controls_tree.parent,
        all_access_controls.aco_id = access_controls_tree.current as "direct"
    FROM
        access_controls_tree
    INNER JOIN
        all_access_controls
    ON
        all_access_controls.parent = access_controls_tree.current
)
SELECT DISTINCT
    concat(all_access_controls.aco_id::text, permissions.per_id::text) AS "acp_id",
    all_access_controls.aco_id AS "aco_id",
    permissions.per_id,
    permissions.entity::text,
    permissions.action::text,
    permissions.lib_id,
    permissions.insert_date,
    permissions.update_date,
    not all_access_controls.direct as "direct"
FROM
    all_access_controls
LEFT OUTER JOIN
    access_controls_permissions
on
    access_controls_permissions.aco_id = all_access_controls.parent
LEFT OUTER JOIN
    permissions
on
    access_controls_permissions.per_id = permissions.per_id
WHERE permissions.per_id IS NOT NULL
ORDER BY all_access_controls.aco_id, permissions.per_id;

-- Unsupported by cockroachdb
-- COMMENT ON VIEW access_controls_permissions_view IS 'This view provides a consolidated look at access controls permissions across the system. It combines information from various tables to present a clear picture of which permissions are assigned to which access_controls. The view employs a recursive CTE (Common Table Expression) to traverse the hierarchical structure of access controls and match them with corresponding permissions. The final output includes unique combinations of accces control and permission IDs, permission details, and associated library IDs.';
