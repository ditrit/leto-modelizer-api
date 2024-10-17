-- Add permission for add/update/delete secret.
ALTER TYPE entity_type ADD VALUE 'AI_SECRET';

INSERT INTO permissions(entity, action, lib_id) VALUES
('AI_SECRET', 'CREATE', NULL),
('AI_SECRET', 'DELETE', NULL),
('AI_SECRET', 'UPDATE', NULL),
('AI_SECRET', 'ACCESS', NULL);

-- Add permission to Super Administrator.
INSERT INTO access_controls_permissions(aco_id, per_id)
SELECT (SELECT aco_id FROM access_controls WHERE name = 'SUPER_ADMINISTRATOR'), per_id FROM permissions WHERE entity = 'AI_SECRET';

-- Add permission to Administrator.
INSERT INTO access_controls_permissions(aco_id, per_id)
SELECT (SELECT aco_id FROM access_controls WHERE name = 'ADMINISTRATOR'), per_id FROM permissions WHERE entity = 'AI_SECRET';

-- Create secret table.
CREATE TABLE IF NOT EXISTS ai_secrets (
    ais_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    key             TEXT NOT NULL UNIQUE,
    value           bytea NOT NULL,
    insert_date     TIMESTAMP NOT NULL DEFAULT now(),
    update_date     TIMESTAMP NOT NULL DEFAULT now()
);

COMMENT ON TABLE  ai_secrets             IS 'Table to store AI secrets.';
COMMENT ON COLUMN ai_secrets.ais_id      IS 'References the primary key in the ai_secrets table.';
COMMENT ON COLUMN ai_secrets.key         IS 'The secret key given by the user, must be unique.';
COMMENT ON COLUMN ai_secrets.value       IS 'The encoded value of the secret.';
COMMENT ON COLUMN ai_secrets.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN ai_secrets.update_date IS 'Last update date of this row.';
