-- Initialize benchmark database
CREATE DATABASE IF NOT EXISTS benchmark;

-- Connect to benchmark database
\c benchmark;

-- Create messages table
CREATE TABLE IF NOT EXISTS messages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    timestamp BIGINT NOT NULL,
    data_type VARCHAR(50) NOT NULL,
    value TEXT NOT NULL
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_messages_name ON messages(name);
CREATE INDEX IF NOT EXISTS idx_messages_timestamp ON messages(timestamp);

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE benchmark TO benchmark;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO benchmark;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO benchmark; 