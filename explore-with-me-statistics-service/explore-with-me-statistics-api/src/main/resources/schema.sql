CREATE TABLE IF NOT EXISTS endpoint
(
    id uuid PRIMARY KEY ,
    app VARCHAR(150),
    uri TEXT,
    ip VARCHAR(15),
    timestamp TIMESTAMP WITHOUT TIME ZONE
);