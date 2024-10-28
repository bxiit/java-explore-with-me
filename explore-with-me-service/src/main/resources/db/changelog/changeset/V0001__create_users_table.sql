CREATE TABLE users
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(254) CHECK ( LENGTH(name) > 2 AND LENGTH(name) < 255 ),
    email   VARCHAR(254) UNIQUE CHECK ( LENGTH(email) > 5 AND LENGTH(email) < 255 ),
    created TIMESTAMP WITHOUT TIME ZONE
);