-- Ensure repeatable migration is really repeated on every start
-- ${flyway:timestamp}

DROP VIEW IF EXISTS access_controls_tree_view;

CREATE VIEW access_controls_tree_view AS
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
SELECT
    concat(all_access_controls.aco_id::text, all_access_controls.parent::text) AS "aca_id",
    all_access_controls.aco_id,
    access_controls.name,
    access_controls.type::text,
    all_access_controls.parent,
    parent_access_controls.name as "parent_name",
    parent_access_controls.type::text as "parent_type",
    all_access_controls.direct
FROM
    all_access_controls
LEFT OUTER JOIN
    access_controls
on
    all_access_controls.aco_id = access_controls.aco_id
LEFT OUTER JOIN
    access_controls as parent_access_controls
on
    all_access_controls.parent = parent_access_controls.aco_id
where
    all_access_controls.aco_id <> all_access_controls.parent;

COMMENT ON VIEW access_controls_tree_view IS 'This view provides a consolidated look at access controls tree across the system. It combines information from various tables to present a clear picture of which access controls are assigned to access controls. The view employs a recursive CTE (Common Table Expression) to traverse the hierarchical structure of access controls. The final output includes unique combinations of IDs and access control details.';
