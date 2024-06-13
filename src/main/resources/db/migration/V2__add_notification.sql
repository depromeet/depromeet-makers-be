CREATE TABLE notifications
(
    id         CHAR(26)     NOT NULL,
    member_id  CHAR(26)     NOT NULL,
    content    VARCHAR(255) NOT NULL,
    type       VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    read_at    datetime NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);
