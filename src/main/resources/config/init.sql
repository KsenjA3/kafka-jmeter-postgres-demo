-- Initialize benchmark database
CREATE DATABASE IF NOT EXISTS benchmark;

-- Connect to benchmark database
\c benchmark;

-- Create messages table
CREATE TABLE IF NOT EXISTS messages (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(255),
    sequence_number BIGINT
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_messages_created_at ON messages(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_messages_source ON messages(source);
CREATE INDEX IF NOT EXISTS idx_messages_sequence_number ON messages(sequence_number DESC);

-- Create sequence for sequence_number if needed
CREATE SEQUENCE IF NOT EXISTS messages_sequence_number_seq;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE benchmark TO benchmark;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO benchmark;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO benchmark; 