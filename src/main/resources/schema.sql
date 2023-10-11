CREATE TABLE IF NOT EXISTS brands
(
    slug TEXT         NOT NULL PRIMARY KEY CHECK ( CHARACTER_LENGTH(slug) > 0 ),
    name VARCHAR(100) NOT NULL
);

CREATE INDEX IF NOT EXISTS index_brands_name ON brands (name);
