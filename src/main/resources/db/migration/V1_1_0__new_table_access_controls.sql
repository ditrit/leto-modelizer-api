CREATE TYPE access_control_type AS ENUM ('ROLE', 'GROUP', 'SCOPE');

CREATE TABLE IF NOT EXISTS access_controls (
    aco_id      SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    type        access_control_type NOT NULL,
    insert_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (name, type)
);

-- Unsupported by cockroachdb
-- COMMENT ON TYPE   access_control_type         IS 'This ENUM specifies the different categories of access control that can be applied to resources or operations. [ROLE]: Represents a specific role that users or groups can be assigned to. [GROUP]: Represents a group of users. [SCOPE]: Represents the scope or context within which access control is applied.';
COMMENT ON TABLE  access_controls             IS 'Table to store access control records, each with a unique combination of name and type.';
COMMENT ON COLUMN access_controls.aco_id      IS 'Primary key, serial.';
COMMENT ON COLUMN access_controls.name        IS 'The name of the access control entry.';
COMMENT ON COLUMN access_controls.type        IS 'The type of access control, defined by the access_control_type ENUM.';
COMMENT ON COLUMN access_controls.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN access_controls.update_date IS 'Last update date of this row.';

INSERT INTO access_controls(aco_id, name, type) VALUES
(1, 'SUPER_ADMINISTRATOR', 'ROLE'),
(2, 'ADMINISTRATOR', 'ROLE'),
(3, 'DEVELOPER', 'ROLE');

ALTER SEQUENCE access_controls_aco_id_seq RESTART WITH 4;
