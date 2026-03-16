INSERT INTO account (account_id, name) VALUES (1, '테스트');
INSERT INTO account (account_id, name) VALUES (2, '김철수');
INSERT INTO account (account_id, name) VALUES (3, '이영희');
INSERT INTO account (account_id, name) VALUES (4, '박지성');
INSERT INTO account (account_id, name) VALUES (5, '손흥민');
INSERT INTO account (account_id, name) VALUES (6, '차범근');
INSERT INTO account (account_id, name) VALUES (7, '김김김');
INSERT INTO account (account_id, name) VALUES (8, '페이커');
INSERT INTO account (account_id, name) VALUES (9, '뉴진스');
INSERT INTO account (account_id, name) VALUES (101, '자동차');
INSERT INTO account (account_id, name) VALUES (707, '비행기');

INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (10000001, 1, 2, 'ACCEPTED', '2026-03-15 10:00:00', '2026-03-15 11:00:00');
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (10000002, 1, 3, 'ACCEPTED', '2026-03-15 09:00:00', '2026-03-15 15:00:00');
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000013, 4, 1, 'ACCEPTED', '2026-03-15 10:00:00', '2026-03-15 12:00:00');
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000014, 5, 1, 'ACCEPTED', '2026-03-15 14:00:00', '2026-03-15 09:30:00');
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000015, 6, 1, 'PENDING', '2026-03-15 11:00:00', NULL);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000016, 7, 1, 'PENDING', '2026-03-15 10:00:00', NULL);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000017, 2, 1, 'PENDING', '2026-03-15 11:00:00', NULL);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000018, 3, 1, 'PENDING', '2026-03-15 11:00:00', NULL);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000019, 4, 1, 'PENDING', '2026-03-15 11:00:00', NULL);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000050, 1, 8, 'ACCEPTED', '2026-03-15 12:00:00', '2026-03-15 13:00:00');
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, rejected_at,
                    approved_at)
VALUES (100000051, 9, 1, 'REJECTED', '2026-03-15 10:00:00', '2026-03-15 11:00:00', NULL);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000052, 9, 101, 'ACCEPTED', '2026-03-15 10:00:00', '2026-03-15 10:00:00');
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (100000053, 101, 9, 'ACCEPTED', '2026-03-15 10:00:00', '2026-03-15 10:00:00');

-- 거절 수락 테스트
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at)
VALUES (7777101, 101, 707, 'PENDING', CURRENT_TIMESTAMP);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at)
VALUES (7777102, 101, 707, 'PENDING', CURRENT_TIMESTAMP);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at)
VALUES (7777103, 707, 101, 'PENDING', CURRENT_TIMESTAMP);
INSERT INTO friend (friend_request_id, from_account_id, to_account_id, friend_status, requested_at)
VALUES (7777104, 707, 101, 'PENDING', CURRENT_TIMESTAMP);
