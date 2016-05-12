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
    CourseName  VARCHAR(64)
    );
CREATE TABLE IF NOT EXISTS Participant (
    UserId       INT           references Users(UserId),
    CourseId     VARCHAR(10)   references Course(CourseId),
    Function     VARCHAR(64)   NOT NULL
    );
CREATE TABLE IF NOT EXISTS Assignment (
    AssignmentId      SERIAL         PRIMARY KEY,
    CourseId          VARCHAR(10)    references Course(CourseId) NOT NULL,
    Title             VARCHAR(64)    NOT NULL,
    StartDate         timestamp      NOT NULL,
    EndDate           timestamp      NOT NULL,
    MinTime           INT            NOT NULL,
    MaxTime           INT            NOT NULL,
    Published         timestamp,
    GradeScale        VARCHAR(64)    NOT NULL
    );
CREATE TABLE IF NOT EXISTS Submission (
    AssignmentId    INT          references Assignment(AssignmentId) NOT NULL,
    StudentId           INT          references Users(UserId) NOT NULL,
    StudentPublishConsent   Boolean ,
    SubmissionDate      timestamp    NOT NULL,
    Grade               VARCHAR (3),
    TeacherId           INT          references Users(UserId),
    PublishStudentSubmission    Boolean ,
    PRIMARY KEY (AssignmentId, StudentId)
    );
CREATE TABLE IF NOT EXISTS Config (
    UserId              INT         references Users(UserId),
    Language            VARCHAR(64) NOT NULL,
    Email               VARCHAR(100),
    TextSize            INT         NOT NULL,
    );