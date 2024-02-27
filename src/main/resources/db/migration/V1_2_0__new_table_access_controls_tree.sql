CREATE TABLE IF NOT EXISTS access_controls_tree (
    act_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    parent      UUID REFERENCES access_controls(aco_id) ON DELETE CASCADE NOT NULL,
    current     UUID REFERENCES access_controls(aco_id) ON DELETE CASCADE NOT NULL,
    insert_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (parent, current)
);

COMMENT ON TABLE  access_controls_tree             IS 'This table represents the hierarchical structure of access controls.';
COMMENT ON COLUMN access_controls_tree.act_id      IS 'Primary key, serial.';
COMMENT ON COLUMN access_controls_tree.parent      IS 'It indicates the parent access control entry for the current entry. References the aco_id in the access_controls table.';
COMMENT ON COLUMN access_controls_tree.current     IS 'It represents the current access control entry in the hierarchical structure. References the aco_id in the access_controls table.';
COMMENT ON COLUMN access_controls_tree.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN access_controls_tree.update_date IS 'Last update date of this row.';
