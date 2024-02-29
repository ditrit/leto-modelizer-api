CREATE TABLE IF NOT EXISTS users (
    usr_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login       VARCHAR(255) NOT NULL UNIQUE,
    email       VARCHAR(255),
    name        VARCHAR(510),
    picture     TEXT,
    insert_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now()
);

COMMENT ON TABLE  users             IS 'Table to store user.';
COMMENT ON COLUMN users.usr_id      IS 'Primary key, serial.';
COMMENT ON COLUMN users.login       IS 'Login of the user.';
COMMENT ON COLUMN users.email       IS 'Email of the user.';
COMMENT ON COLUMN users.name        IS 'Full name of the user.';
COMMENT ON COLUMN users.picture     IS 'URL link to the user profile picture.';
COMMENT ON COLUMN users.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN users.update_date IS 'Last update date of this row.';
