CREATE TABLE IF NOT EXISTS libraries (
    lib_id            SERIAL PRIMARY KEY,
    url               TEXT NOT NULL UNIQUE,
    name              VARCHAR(255) NOT NULL,
    version           VARCHAR(255),
    maintainer        VARCHAR(255),
    description       TEXT,
    icon              TEXT,
    documentation_url TEXT,
    insert_date       TIMESTAMP NOT NULL DEFAULT now(),
    update_date       TIMESTAMP NOT NULL DEFAULT now()
);

COMMENT ON TABLE  libraries                   IS 'This table stores information about various libraries used within the diagram system.';
COMMENT ON COLUMN libraries.lib_id            IS 'Primary key, serial.';
COMMENT ON COLUMN libraries.url               IS 'The URL of the library. This field is mandatory and unique, ensuring that each library is associated with a distinct URL, which is a link to the index of source code repository.';
COMMENT ON COLUMN libraries.name              IS 'The name of the library.';
COMMENT ON COLUMN libraries.version           IS 'The version of the library.';
COMMENT ON COLUMN libraries.maintainer        IS 'The maintainer of the library. Reference of username.';
COMMENT ON COLUMN libraries.description       IS 'A text description of the library.';
COMMENT ON COLUMN libraries.icon              IS 'Path of library icon.';
COMMENT ON COLUMN libraries.documentation_url IS 'URL of the library documentation.';
COMMENT ON COLUMN libraries.insert_date       IS 'Creation date of this row.';
COMMENT ON COLUMN libraries.update_date       IS 'Last update date of this row.';
