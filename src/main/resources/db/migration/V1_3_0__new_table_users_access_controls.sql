CREATE TABLE IF NOT EXISTS users_access_controls (
    uac_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usr_id      UUID REFERENCES users(usr_id) ON DELETE CASCADE NOT NULL,
    aco_id      UUID REFERENCES access_controls(aco_id) ON DELETE CASCADE NOT NULL,
    insert_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (usr_id, aco_id)
);

COMMENT ON TABLE  users_access_controls             IS 'This table links users to access control entries, defining what access controls are applicable to each user.';
COMMENT ON COLUMN users_access_controls.uac_id      IS 'Primary key, serial.';
COMMENT ON COLUMN users_access_controls.usr_id      IS 'It indicates the parent access control entry for the current entry. References the aco_id in the access_controls table.';
COMMENT ON COLUMN users_access_controls.aco_id      IS 'It represents the current access control entry in the hierarchical structure. References the aco_id in the access_controls table.';
COMMENT ON COLUMN users_access_controls.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN users_access_controls.update_date IS 'Last update date of this row.';
