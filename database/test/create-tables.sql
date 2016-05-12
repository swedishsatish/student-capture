CREATE TABLE IF NOT EXISTS Users (
    UserId       SERIAL         PRIMARY KEY,
    UserSalt     VARCHAR(128),
    UserName     VARCHAR(64)    UNIQUE,
    FirstName    VARCHAR(64)    NOT NULL,
    LastName     VARCHAR(64)    NOT NULL,
    Email        TEXT           NOT NULL,
    Pswd         VARCHAR(64)    NOT NULL
    );

CREATE TABLE IF NOT EXISTS Course (
    CourseId    VARCHAR(10)    PRIMARY KEY,
    Year        INT            NOT NULL,
    Term        VARCHAR(8)     NOT NULL,
    CourseCode  VARCHAR(8)     NOT NULL,
    CourseName  VARCHAR(64),
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
    Title	          VARCHAR(64)	 NOT NULL,
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