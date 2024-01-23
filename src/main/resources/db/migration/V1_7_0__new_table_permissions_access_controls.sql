CREATE TABLE IF NOT EXISTS permissions_access_controls (
    pac_id      SERIAL PRIMARY KEY,
    per_id      INTEGER REFERENCES permissions(per_id) ON DELETE CASCADE NOT NULL,
    aco_id      INTEGER REFERENCES access_controls(aco_id) ON DELETE CASCADE NOT NULL,
    insert_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (per_id, aco_id)
);

COMMENT ON TABLE  permissions_access_controls             IS 'This table establishes a relationship between permissions and access controls. It maps which access controls are associated with specific permissions.';
COMMENT ON COLUMN permissions_access_controls.pac_id      IS 'Primary key, serial.';
COMMENT ON COLUMN permissions_access_controls.per_id      IS 'This column denotes the specific permission being linked to an access control. References the per_id in the permissions table. ';
COMMENT ON COLUMN permissions_access_controls.aco_id      IS 'This column indicates the specific access control entry that is associated with the given permission. References the aco_id in the access_controls table.';
COMMENT ON COLUMN permissions_access_controls.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN permissions_access_controls.update_date IS 'Last update date of this row.';

-- Add all permissions to Super administrator.
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 1);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 2);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 3);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 4);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 5);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 6);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 7);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 8);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (1, 9);

-- Add permission ['ADMIN', 'ACCESS', NULL] to Administrator.
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (2, 1);

-- Add permission of all actions on leto-modelizer to Developer.
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (3, 2);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (3, 3);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (3, 4);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (3, 5);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (3, 6);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (3, 7);
INSERT INTO permissions_access_controls(aco_id, per_id) VALUES (3, 8);
