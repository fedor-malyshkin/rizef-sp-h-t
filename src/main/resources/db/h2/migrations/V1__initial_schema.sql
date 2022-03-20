CREATE TABLE CATEGORIES
(
    id       INTEGER     NOT NULL PRIMARY KEY,
    category VARCHAR(20) NOT NULL
);

INSERT INTO CATEGORIES (id, category)
VALUES (0, 'ACTOR');
INSERT INTO CATEGORIES (id, category)
VALUES (1, 'PAINTER');
INSERT INTO CATEGORIES (id, category)
VALUES (2, 'SCULPTOR');


CREATE TABLE artists
(
    id          IDENTITY    NOT NULL PRIMARY KEY,
    first_name  VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name   VARCHAR(50) NOT NULL,
    category_id INTEGER     NOT NULL,
    birthday    DATE,
    email       VARCHAR(50),
    notes       VARCHAR(200),
    CREATED_AT  TIMESTAMP,
    UPDATED_AT  TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES (ID)
);
