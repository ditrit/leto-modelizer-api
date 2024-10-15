CREATE TABLE IF NOT EXISTS access_controls_permissions (
    pac_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    per_id      UUID REFERENCES permissions(per_id) ON DELETE CASCADE NOT NULL,
    aco_id      UUID REFERENCES access_controls(aco_id) ON DELETE CASCADE NOT NULL,
    insert_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (per_id, aco_id)
);

COMMENT ON TABLE  access_controls_permissions             IS 'This table establishes a relationship between permissions and access controls. It maps which access controls are associated with specific permissions.';
COMMENT ON COLUMN access_controls_permissions.pac_id      IS 'Primary key, serial.';
COMMENT ON COLUMN access_controls_permissions.per_id      IS 'This column denotes the specific permission being linked to an access control. References the per_id in the permissions table.';
COMMENT ON COLUMN access_controls_permissions.aco_id      IS 'This column indicates the specific access control entry that is associated with the given permission. References the aco_id in the access_controls table.';
COMMENT ON COLUMN access_controls_permissions.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN access_controls_permissions.update_date IS 'Last update date of this row.';

-- Add all permissions to Super administrator.
INSERT INTO access_controls_permissions(aco_id, per_id)
SELECT (SELECT aco_id FROM access_controls WHERE name = 'SUPER_ADMINISTRATOR'), per_id FROM permissions;

-- Add permission ['ADMIN', 'ACCESS', NULL] to Administrator.
INSERT INTO access_controls_permissions(aco_id, per_id)
SELECT (SELECT aco_id FROM access_controls WHERE name = 'ADMINISTRATOR'), per_id FROM permissions WHERE entity = 'ADMIN' AND action = 'ACCESS' AND lib_id IS NULL;

-- Add permission of all actions on leto-modelizer to Developer.
INSERT INTO access_controls_permissions(aco_id, per_id)
SELECT (SELECT aco_id FROM access_controls WHERE name = 'DEVELOPER'), per_id FROM permissions WHERE entity in ('PROJECT', 'PROJECT_GIT', 'PROJECT_TEMPLATE', 'DIAGRAM', 'DIAGRAM_TEMPLATE', 'COMPONENT', 'COMPONENT_TEMPLATE');
