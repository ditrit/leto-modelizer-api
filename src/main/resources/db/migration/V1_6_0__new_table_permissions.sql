CREATE TYPE entity_type AS ENUM ('ADMIN', 'PROJECT', 'PROJECT_TEMPLATE', 'DIAGRAM', 'DIAGRAM_TEMPLATE', 'COMPONENT', 'COMPONENT_TEMPLATE', 'LIBRARY');
CREATE TYPE action_type AS ENUM ('ACCESS', 'CREATE', 'DELETE');

CREATE TABLE IF NOT EXISTS permissions (
    per_id      SERIAL PRIMARY KEY,
    entity      entity_type NOT NULL,
    action      action_type NOT NULL,
    lib_id      INTEGER REFERENCES libraries(lib_id) ON DELETE CASCADE,
    insert_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (entity, action, lib_id)
);

COMMENT ON TYPE   entity_type             IS 'An ENUM type representing the various types of entities for which permissions can be set.';
COMMENT ON TYPE   action_type             IS 'An ENUM type representing the different actions that can be controlled through permissions.';

COMMENT ON TABLE  permissions             IS 'This table stores permission settings for various entities within the system.';
COMMENT ON COLUMN permissions.per_id      IS 'Primary key, serial.';
COMMENT ON COLUMN permissions.entity      IS 'Specifies the type of entity for which the permission is being set, based on the entity_type ENUM. ';
COMMENT ON COLUMN permissions.action      IS 'Defines the action that is permissible for the entity, based on the action_type ENUM.';
COMMENT ON COLUMN permissions.lib_id      IS 'References the lib_id in the libraries table. This optional field links the permission to a specific library, allowing for permissions to be set at a library-specific level.';
COMMENT ON COLUMN permissions.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN permissions.update_date IS 'Last update date of this row.';

INSERT INTO permissions(per_id, entity, action, lib_id) VALUES
(1, 'ADMIN', 'ACCESS', NULL),
(2, 'PROJECT', 'CREATE', NULL),
(3, 'PROJECT_TEMPLATE', 'CREATE', NULL),
(4, 'DIAGRAM', 'CREATE', NULL),
(5, 'DIAGRAM', 'DELETE', NULL),
(6, 'DIAGRAM_TEMPLATE', 'CREATE', NULL),
(7, 'COMPONENT', 'CREATE', NULL),
(8, 'COMPONENT_TEMPLATE', 'CREATE', NULL),
(9, 'LIBRARY', 'ACCESS', NULL);
