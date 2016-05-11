CREATE TABLE IF NOT EXISTS Users (
    UserId       SERIAL         PRIMARY KEY,
    UserName     VARCHAR(64)    UNIQUE,
    FirstName    VARCHAR(64)    NOT NULL,
    LastName     VARCHAR(64)    NOT NULL,
    PersNr       CHAR(10)       NOT NULL,
    Pswd         VARCHAR(64)    NOT NULL
    );
