CREATE TABLE attendances
(
    id                CHAR(26)     NOT NULL,
    generation        INT          NOT NULL,
    week              INT          NOT NULL,
    member_id         CHAR(26)     NOT NULL,
    session_type      VARCHAR(255) NOT NULL,
    attendance_status VARCHAR(255) NOT NULL,
    attendance_time   datetime     NULL,
    CONSTRAINT pk_attendances PRIMARY KEY (id)
);

CREATE TABLE member
(
    id       CHAR(26)     NOT NULL,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    passcord VARCHAR(255) NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

CREATE TABLE member_generation
(
    member_id     CHAR(26)     NOT NULL,
    generation_id INT          NOT NULL,
    group_id      INT          NULL,
    `role`        VARCHAR(255) NOT NULL,
    position      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_member_generation PRIMARY KEY (member_id, generation_id)
);

CREATE TABLE session
(
    id            CHAR(26)     NOT NULL,
    generation    INT          NOT NULL,
    week          INT          NOT NULL,
    title         VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    start_time    datetime     NOT NULL,
    session_type  VARCHAR(255) NOT NULL,
    address       VARCHAR(255) NULL,
    longitude     DOUBLE       NULL,
    latitude      DOUBLE       NULL,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

ALTER TABLE member
    ADD CONSTRAINT uc_member_email UNIQUE (email);

ALTER TABLE attendances
    ADD CONSTRAINT FK_ATTENDANCES_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE member_generation
    ADD CONSTRAINT FK_MEMBER_GENERATION_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);
