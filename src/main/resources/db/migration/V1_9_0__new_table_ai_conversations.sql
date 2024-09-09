CREATE TABLE IF NOT EXISTS ai_conversations (
    aic_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usr_id          UUID REFERENCES users(usr_id) ON DELETE CASCADE NOT NULL,
    key             TEXT NOT NULL,
    checksum        TEXT,
    context         TEXT,
    size            BIGINT,
    insert_date     TIMESTAMP NOT NULL DEFAULT now(),
    update_date     TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT      uc_aic_usr_id_key UNIQUE (usr_id, key)
);

COMMENT ON TABLE  ai_conversations             IS 'Table to store AI chat conversation.';
COMMENT ON COLUMN ai_conversations.aic_id      IS 'Primary key, serial.';
COMMENT ON COLUMN ai_conversations.usr_id      IS 'It indicates the user for the current entry. References the primary key in the users table.';
COMMENT ON COLUMN ai_conversations.key         IS 'Concatenation of project name, diagram path and plugin name.';
COMMENT ON COLUMN ai_conversations.checksum    IS 'Checksum of last diagram files sent.';
COMMENT ON COLUMN ai_conversations.context     IS 'AI context of conversation.';
COMMENT ON COLUMN ai_conversations.size        IS 'Size of all conversation messages.';
COMMENT ON COLUMN ai_conversations.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN ai_conversations.update_date IS 'Last update date of this row.';

COMMENT ON CONSTRAINT uc_aic_usr_id_key ON ai_conversations IS 'Last update date of this row.';
