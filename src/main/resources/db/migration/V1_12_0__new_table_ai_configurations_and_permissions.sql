-- Add permission for add/update/delete configuration.
ALTER TYPE entity_type ADD VALUE 'AI_CONFIGURATION';

INSERT INTO permissions(entity, action, lib_id) VALUES
('AI_CONFIGURATION', 'CREATE', NULL),
('AI_CONFIGURATION', 'DELETE', NULL),
('AI_CONFIGURATION', 'UPDATE', NULL),
('AI_CONFIGURATION', 'ACCESS', NULL);

-- Add permission to Super Administrator.
INSERT INTO access_controls_permissions(aco_id, per_id)
SELECT (SELECT aco_id FROM access_controls WHERE name = 'SUPER_ADMINISTRATOR'), per_id FROM permissions WHERE entity = 'AI_CONFIGURATION';

-- Add permission to Administrator.
INSERT INTO access_controls_permissions(aco_id, per_id)
SELECT (SELECT aco_id FROM access_controls WHERE name = 'ADMINISTRATOR'), per_id FROM permissions WHERE entity = 'AI_CONFIGURATION';

-- Create configuration table.
CREATE TABLE IF NOT EXISTS ai_configurations (
    acf_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    key             TEXT NOT NULL UNIQUE,
    value           TEXT NOT NULL,
    insert_date     TIMESTAMP NOT NULL DEFAULT now(),
    update_date     TIMESTAMP NOT NULL DEFAULT now()
);

COMMENT ON TABLE  ai_configurations             IS 'Table to store AI configurations.';
COMMENT ON COLUMN ai_configurations.acf_id      IS 'References the primary key in the ai_configurations table.';
COMMENT ON COLUMN ai_configurations.key         IS 'The configuration key given by the user, must be unique.';
COMMENT ON COLUMN ai_configurations.value       IS 'The encoded value of the configuration.';
COMMENT ON COLUMN ai_configurations.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN ai_configurations.update_date IS 'Last update date of this row.';
