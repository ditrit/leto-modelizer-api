CREATE TABLE IF NOT EXISTS ai_messages (
    aim_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aic_id          UUID REFERENCES ai_conversations(aic_id) ON DELETE CASCADE NOT NULL,
    is_user         BOOLEAN NOT NULL,
    message         bytea,
    insert_date     TIMESTAMP NOT NULL DEFAULT now(),
    update_date     TIMESTAMP NOT NULL DEFAULT now()
);

COMMENT ON TABLE  ai_messages             IS 'Table to store AI chat messages.';
COMMENT ON COLUMN ai_messages.aic_id      IS 'It indicates the conversation for the current entry. References the primary key in the ai_conversations table.';
COMMENT ON COLUMN ai_messages.is_user     IS 'Indicate the author of the message: if true, the message is from the user; otherwise, it is from the AI.';
COMMENT ON COLUMN ai_messages.message     IS 'Base64 of compressed message.';
COMMENT ON COLUMN ai_messages.insert_date IS 'Creation date of this row.';
COMMENT ON COLUMN ai_messages.update_date IS 'Last update date of this row.';
