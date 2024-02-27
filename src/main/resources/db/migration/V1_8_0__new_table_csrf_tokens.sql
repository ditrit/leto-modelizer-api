CREATE TABLE IF NOT EXISTS csrf_tokens (
    cst_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login           VARCHAR(255) REFERENCES users(login) ON DELETE CASCADE NOT NULL UNIQUE,
    token           TEXT,
    expiration_date TIMESTAMP NOT NULL,
    insert_date     TIMESTAMP NOT NULL DEFAULT now(),
    update_date     TIMESTAMP NOT NULL DEFAULT now()
);

COMMENT ON TABLE  csrf_tokens                 IS 'Table to store CSRF token.';
COMMENT ON COLUMN csrf_tokens.cst_id          IS 'Primary key, serial.';
COMMENT ON COLUMN csrf_tokens.login           IS 'It indicates the user for the current entry. References the login in the users table.';
COMMENT ON COLUMN csrf_tokens.token           IS 'Token value store.';
COMMENT ON COLUMN csrf_tokens.expiration_date IS 'Expiration date of this token.';
COMMENT ON COLUMN csrf_tokens.insert_date     IS 'Creation date of this row.';
COMMENT ON COLUMN csrf_tokens.update_date     IS 'Last update date of this row.';
