CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    elo_score INTEGER NOT NULL DEFAULT 1000,
    name VARCHAR(255),
    bio TEXT,
    image TEXT
);

CREATE TABLE tournaments (
    id VARCHAR(255) PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    winner_user_id BIGINT NULL,
    CONSTRAINT fk_winner_user_id FOREIGN KEY (winner_user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS pushup_records (
    id SERIAL PRIMARY KEY,
    count INTEGER NOT NULL,
    user_id BIGINT NOT NULL,
    duration BIGINT NOT NULL, -- Duration in seconds or milliseconds based on your preference
    timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    tournament_id VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id), -- FK constraint to link to the User
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id) -- FK constraint to link to the Tournament
    -- Consider adding an index on tournament_id and user_id for faster lookups
);