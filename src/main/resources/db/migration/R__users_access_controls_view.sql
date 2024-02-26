-- Ensure repeatable migration is really repeated on every start
-- ${flyway:timestamp}

DROP VIEW IF EXISTS users_access_controls_view;

CREATE VIEW users_access_controls_view AS
WITH RECURSIVE all_access_controls AS (
    SELECT
        aco_id,
        usr_id,
        FALSE as "direct"
    FROM
        users_access_controls
    UNION
    SELECT
        parent,
        usr_id,
        all_access_controls.aco_id = access_controls_tree.current as "direct"
    FROM
        access_controls_tree
    INNER JOIN
        all_access_controls
    ON
        all_access_controls.aco_id = access_controls_tree.current
)
SELECT DISTINCT
    concat(users.usr_id::text, access_controls.aco_id::text) AS "usa_id",
    users.usr_id,
    users.login,
    users.email,
    users.name as "user_name",
    access_controls.aco_id,
    access_controls.name as "access_control_name",
    access_controls.type::text,
    not all_access_controls.direct as "direct"
FROM
    all_access_controls
LEFT OUTER JOIN
    access_controls
ON
    access_controls.aco_id = all_access_controls.aco_id
LEFT OUTER JOIN
    users
ON
    users.usr_id = all_access_controls.usr_id;

-- Unsupported by cockroachdb
-- COMMENT ON VIEW users_access_controls_view IS 'This view provides a consolidated look at user access controls across the system. It combines information from various tables to present a clear picture of which access controls are assigned to which users. The view employs a recursive CTE (Common Table Expression) to traverse the hierarchical structure of access controls. The final output includes unique combinations of IDs and access control details.';
