DROP TABLE IF EXISTS friend;
DROP TABLE IF EXISTS account;

CREATE TABLE account
(
    account_id BIGINT NOT NULL,
    name       VARCHAR(255),
    CONSTRAINT pk_account PRIMARY KEY (account_id)
);

CREATE TABLE friend
(
    friend_request_id BIGINT      NOT NULL,
    from_account_id   BIGINT      NOT NULL,
    to_account_id     BIGINT      NOT NULL,
    friend_status     VARCHAR(20) NOT NULL, -- PENDING, ACCEPTED, REJECTED
    requested_at      TIMESTAMP,
    approved_at       TIMESTAMP,
    rejected_at       TIMESTAMP,
    CONSTRAINT pk_friend PRIMARY KEY (friend_request_id),
    CONSTRAINT fk_friend_on_from_account FOREIGN KEY (from_account_id) REFERENCES account (account_id),
    CONSTRAINT fk_friend_on_to_account FOREIGN KEY (to_account_id) REFERENCES account (account_id)
);
CREATE INDEX idx_friend_from_account ON friend (from_account_id);
CREATE INDEX idx_friend_to_account ON friend (to_account_id);