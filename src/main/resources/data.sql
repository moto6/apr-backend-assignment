-- 1. Account 데이터 (10명)
INSERT INTO account (account_id, name) VALUES (1, '나(Tester)');
INSERT INTO account (account_id, name) VALUES (2, '김철수');
INSERT INTO account (account_id, name) VALUES (3, '이영희');
INSERT INTO account (account_id, name) VALUES (4, '박지성');
INSERT INTO account (account_id, name) VALUES (5, '손흥민');
INSERT INTO account (account_id, name) VALUES (6, '차범근');
INSERT INTO account (account_id, name) VALUES (7, '김연아');
INSERT INTO account (account_id, name) VALUES (8, '페이커');
INSERT INTO account (account_id, name) VALUES (9, '뉴진스');
INSERT INTO account (account_id, name) VALUES (10, '아이브');

-- 2. Friend 데이터 (10개)
-- 1번 유저(나)를 기준으로 다양한 케이스 구성

-- Case A: 내가 신청해서 수락됨 (ACCEPTED)
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (1, 1, 2, 'ACCEPTED', '2024-03-10 10:00:00', '2024-03-11 11:00:00');
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (2, 1, 3, 'ACCEPTED', '2024-03-12 09:00:00', '2024-03-12 15:00:00');

-- Case B: 상대가 신청해서 내가 수락함 (ACCEPTED)
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (3, 4, 1, 'ACCEPTED', '2024-03-13 10:00:00', '2024-03-13 12:00:00');
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (4, 5, 1, 'ACCEPTED', '2024-03-14 14:00:00', '2024-03-15 09:30:00');

-- Case C: 아직 수락 대기 중 (PENDING) -> 목록 조회 시 나오지 않아야 함
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (5, 1, 6, 'PENDING', '2024-03-15 10:00:00', NULL);
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (6, 7, 1, 'PENDING', '2024-03-15 11:00:00', NULL);

-- Case D: 최근 수락된 친구 (정렬 확인용)
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (7, 1, 8, 'ACCEPTED', '2024-03-15 12:00:00', '2024-03-15 13:00:00');

-- Case E: 거절됨 (REJECTED) -> 목록 조회 시 나오지 않아야 함
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (8, 9, 1, 'REJECTED', '2024-03-01 10:00:00', NULL);

-- Case F: 나랑 아예 상관없는 유저들끼리의 관계 -> 목록 조회 시 나오지 않아야 함
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (9, 9, 10, 'ACCEPTED', '2024-03-01 10:00:00', '2024-03-02 10:00:00');
INSERT INTO friend (friend_id, from_account_id, to_account_id, friend_status, requested_at, approved_at)
VALUES (10, 10, 9, 'ACCEPTED', '2024-03-05 10:00:00', '2024-03-06 10:00:00');