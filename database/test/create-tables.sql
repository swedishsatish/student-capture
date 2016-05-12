CREATE TABLE IF NOT EXISTS Users (
    UserId       SERIAL         PRIMARY KEY,
    UserName     VARCHAR(64)    UNIQUE,
    FirstName    VARCHAR(64)    NOT NULL,
    LastName     VARCHAR(64)    NOT NULL,
    PersNr       CHAR(10)       NOT NULL,
    Pswd         VARCHAR(64)    NOT NULL
    );

CREATE TABLE IF NOT EXISTS Course (
    CourseId    VARCHAR(10)    PRIMARY KEY,
    Year        INT            NOT NULL,
    Term        CHAR(4)        NOT NULL,
    CourseCode  CHAR(6)        NOT NULL,
    CourseName  TEXT,
    Active      BOOLEAN        NOT NULL
    );

CREATE TABLE IF NOT EXISTS Participant (
    UserId       INT           references Users(UserId),
    CourseId     VARCHAR(10)   references Course(CourseId),
    Function     VARCHAR(32)   NOT NULL
    );

CREATE TABLE IF NOT EXISTS Assignment (
    AssignmentId      SERIAL         PRIMARY KEY,
    CourseId          VARCHAR(10)    references Course(CourseId) NOT NULL,
    Title	          TEXT	         NOT NULL,
    StartDate         timestamp      NOT NULL,
    EndDate           timestamp      NOT NULL,
    MinTime           INT            NOT NULL,
    MaxTime           INT            NOT NULL,
    Published         boolean        NOT NULL
    );

CREATE TABLE IF NOT EXISTS Submission (
    AssignmentId	INT          references Assignment(AssignmentId) NOT NULL,
    StudentId           INT          references Users(UserId) NOT NULL,
    SubmissionDate      timestamp    NOT NULL,
    Grade               VARCHAR (3),
    TeacherId           INT          references Users(UserId),
    PRIMARY KEY (AssignmentId, StudentId)
    );