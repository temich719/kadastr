CREATE EXTENSION IF NOT EXISTS "pgcrypto";

------------TABLES---------------------------
CREATE TABLE IF NOT EXISTS roles
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(20)
);

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_JOURNALIST'),
       ('ROLE_SUBSCRIBER');


--- varchar(80)
CREATE TABLE IF NOT EXISTS users
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username       VARCHAR(40) NOT NULL UNIQUE,
    password       TEXT NOT NULL,
    name           VARCHAR(20) NOT NULL,
    surname        VARCHAR(20) NOT NULL,
    "parentName"   VARCHAR(20) NOT NULL,
    "creationDate" TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    "lastEditDate" TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    "roleId"       UUID,
    FOREIGN KEY ("roleId") REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS news
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title          VARCHAR(150)  NOT NULL,
    text           VARCHAR(2000) NOT NULL,
    "creationDate" TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    "lastEditDate" TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    "insertedById" UUID,
    "updatedById"  UUID,
    FOREIGN KEY ("insertedById") REFERENCES users (id),
    FOREIGN KEY ("updatedById") REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text           VARCHAR(300) NOT NULL,
    "creationDate" TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    "lastEditDate" TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    "insertedById" UUID,
    "idNews"       UUID,
    FOREIGN KEY ("insertedById") REFERENCES users (id),
    FOREIGN KEY ("idNews") REFERENCES news (id)
);

------------FUNCTIONS---------------------------
CREATE OR REPLACE FUNCTION set_creation_date()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW."creationDate" := CURRENT_TIMESTAMP;------------change in db this quotes
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION set_edit_date()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW."lastEditDate" := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

------------TRIGGERS---------------------------
CREATE TRIGGER set_creation_date_trigger_users
    BEFORE INSERT
    ON users
    FOR EACH ROW
EXECUTE FUNCTION set_creation_date();

CREATE TRIGGER set_edit_date_trigger_users
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION set_edit_date();

----------------------------------------------
CREATE TRIGGER set_creation_date_trigger_news
    BEFORE INSERT
    ON news
    FOR EACH ROW
EXECUTE FUNCTION set_creation_date();

CREATE TRIGGER set_edit_date_trigger_news
    BEFORE UPDATE
    ON news
    FOR EACH ROW
EXECUTE FUNCTION set_edit_date();

----------------------------------------------
CREATE TRIGGER set_creation_date_trigger_comments
    BEFORE INSERT
    ON comments
    FOR EACH ROW
EXECUTE FUNCTION set_creation_date();

CREATE TRIGGER set_edit_date_trigger_comments
    BEFORE UPDATE
    ON comments
    FOR EACH ROW
EXECUTE FUNCTION set_edit_date();








