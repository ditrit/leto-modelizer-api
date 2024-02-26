CREATE TYPE library_template_type AS ENUM ('PROJECT', 'DIAGRAM', 'COMPONENT');

CREATE TABLE IF NOT EXISTS library_templates (
    lit_id            SERIAL PRIMARY KEY,
    lib_id            INTEGER REFERENCES libraries(lib_id) ON DELETE CASCADE,
    name              VARCHAR(255) NOT NULL,
    type              library_template_type NOT NULL,
    description       TEXT,
    documentation_url TEXT,
    base_path         TEXT,
    plugins           TEXT,
    icon              TEXT,
    schemas           TEXT,
    files             TEXT,
    insert_date       TIMESTAMP NOT NULL DEFAULT now(),
    update_date       TIMESTAMP NOT NULL DEFAULT now()
);

-- Unsupported by cockroachdb
-- COMMENT ON TYPE   library_template_type               IS 'An ENUM type representing the different type of template.';

COMMENT ON TABLE  library_templates                   IS 'This table stores information about various libraries used within the diagram system.';
COMMENT ON COLUMN library_templates.lit_id            IS 'Primary key, serial.';
COMMENT ON COLUMN library_templates.lib_id            IS 'References the lib_id in the libraries table. This optional field links the template to a specific library.';
COMMENT ON COLUMN library_templates.name              IS 'The name of the library template.';
COMMENT ON COLUMN library_templates.type              IS 'Specifies the type of entity for which the template is being set, based on the library_template_type ENUM.';
COMMENT ON COLUMN library_templates.description       IS 'A text description of the library template.';
COMMENT ON COLUMN library_templates.documentation_url IS 'The documentation URL of the library template.';
COMMENT ON COLUMN library_templates.insert_date       IS 'Creation date of this row.';
COMMENT ON COLUMN library_templates.update_date       IS 'Last update date of this row.';
