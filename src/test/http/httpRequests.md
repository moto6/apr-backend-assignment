# API Test Scripts (curl)

## 4.1 친구 목록 조회 (Pagination)

```Bash
curl -X GET "http://localhost:8080/api/v1/friend?page=0&size=10&sort=approvedAt,desc" \
-H "X-User-Id: 1" \
-H "Accept: application/json"
```   

## 4.2 받은 친구 요청 목록 조회 (Cursor/Window 기반)

```Bash
curl -X GET "http://localhost:8080/api/v1/friend/requests?maxSize=20&window=7d" \
-H "X-User-Id: 1" \
-H "Accept: application/json"
```

## 4.3 친구 요청 보내기

```Bash
curl -X POST "http://localhost:8080/api/v1/friend/request" \
-H "X-User-Id: 1" \
-H "Content-Type: application/json" \
-d '{"toAccountId": 2}'
```   

## 4.4 친구 요청 수락

### 요청 수락 1

```Bash
curl -X POST "http://localhost:8080/api/v1/friend/accept/7777101" \
-H "X-User-Id: 707"
```

### 요청 수락 2

```Bash

curl -X POST "http://localhost:8080/api/v1/friend/accept/7777103" \
-H "X-User-Id: 101"
```

## 4.5 친구 요청 거절

### 요청 거절 1

```Bash
curl -X POST "http://localhost:8080/api/v1/friend/reject/7777102" \
-H "X-User-Id: 707"
```

# 요청 거절 2

```Bash
curl -X POST "http://localhost:8080/api/v1/friend/reject/7777104" \
-H "X-User-Id: 101"
```

## 4.6 계정 생성

```Bash
curl -X POST "http://localhost:8080/api/v1/account" \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-d '{"name": "best i friend"}'
```
