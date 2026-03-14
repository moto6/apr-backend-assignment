
# 에이피알 백엔드 직무 과제

## 📋 과제 개요

- 구현에 필요한 도구나 라이브러리는 자유롭게 선택해 사용할 수 있습니다.
- AI 도구 역시 제한 없이 활용하셔도 됩니다.
- 요구사항에 명시되지 않았거나 흐름이 불명확한 부분은 임의로 보완하되, 그 근거를 함께 작성해 주세요.

## 📝 과제 명세서

### 1. 기능 개요

에이지알 서비스에는 친구 맺기 기능이 있습니다. 해당 기능은 사용자 간의 상호작용을 지원하는 API 구현 과제입니다.

- 친구 관계는 **A 사용자의 신청**과 **B 사용자의 수락/거절**을 통해 성립됩니다.
- A가 친구 신청을 하면, B가 이를 **승인해야만 친구 관계**가 형성됩니다.
- B가 거절할 경우에는 **친구 관계가 성립되지 않습니다.**

### 2. 기능 요구사항

- **친구 신청:** 다른 사용자에게 친구 신청
- **친구 수락/거절:** 받은 친구 신청에 대한 응답
- **친구 목록 조회:** 현재 맺어진 친구 목록 조회
- **친구 수 제한:** 최대 10,000명까지만 친구 수락이 가능함

### 3. 기술적 요구사항

아래 기술 스택을 포함하여 구현하고, 코드는 Layered Architecture (Controller / Service / Repository) 형태로 작성해야 합니다.

- **Framework:** SpringBoot 3.X
- **Database:** H2 (in-memory)
- **ORM:** Spring Data JPA
- **Build Tool:** Gradle
- **JAVA:** 21

### 4. 필수 구현 API 목록

### 4-1. 친구 목록 조회

- 나를 기준으로 현재 맺어진 친구 목록을 조회합니다.
- Pageable 결과를 반환하며, approvedAt 기준으로 정렬할 수 있어야 합니다.

```
GET /api/friends?page=0&maxSize=20&sort=approvedAt,desc
```

**Response 200:**

```json
{
  "data": {
    "totalPages": 0,
    "totalCount": 20,
    "items": [
      {
        "user_id": 1,
        "from_user_id": 2,
        "to_user_id": 1,
        "approvedAt": "2024-01-19T06:15:25.801Z"
      },
      ...(중략),
      {
        "user_id": 20,
        "from_user_id": 20,
        "to_user_id": 100044737,
        "approvedAt": "2025-06-22T13:29:25.201Z"
      }
    ]
  }
}
```

### 4-2. 받은 친구 신청 목록 조회

- 나를 기준으로 받은 친구 신청 목록을 조회합니다.
- 슬라이딩 윈도우를 적용해 **현재 시각**을 기준으로 과거 1일(또는 7일, 30일 등) 동안 받은 친구 신청 요청들을 `requestedAt` 기준으로 `DESC` 정렬하여, 해당 범위 내에서 최대 maxSize 개수만큼 데이터를 반환합니다.

| **값** | **의미** |
| --- | --- |
| 1d | 최근 1일 |
| 7d | 최근 7일 |
| 30d | 최근 30일 |
| 90d | 최근 90일 |
| over | 90일 초과 (장기 누적) |

**예시입니다.**

- 현재 시간: `2025-08-22T12:00:00Z`
- window = `1d`
- → 필터링 기준: `requestedAt >= 2025-08-21T12:00:00Z`
- 정렬 기준: `requestedAt DESC`
- 결과: 위 조건을 만족하는 요청 중 최신 순으로 `maxSize` 개수만큼 반환

```
GET /api/friends/requests?maxSize=20&window=1d&sort=requestedAt,desc
```

**Response 200:**

```json
{
  "data": {
    "window": "1d",
    "totalCount": 20,
    "items": [
      {
        "request_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "request_user_id": 1,
        "requestedAt": "2024-01-19T06:15:25.801Z"
      },
      ...(중략),
      {
        "request_id": "2fb85f64-5717-4562-b3fc-2c963f66afa6",
        "request_user_id": 20,
        "requestedAt": "2025-01-20T03:29:25.201Z"
      }
    ]
  }
}
```

### 4-3. 친구 신청

- 요청 시, 상대방의 친구 신청 목록에 데이터가 추가됩니다.
- 친구 신청의 취소의 개념은 고려하지 않습니다.
- 단, **사용자별 초당 10회 요청 제한**이 있으며 초과 시 `429 Too Many Requests`를 반환합니다.
- 본인의 설계에 따라 body는 임의로 필요한 내용을 채우면 됩니다.
- 자기 자신에게 요청, 존재하지 않는 사용자, 이미 보낸 요청 등은 예외 처리해야 하며, 상황에 맞는 HTTP Status Code와 메시지를 반환해야 합니다.
- 상대가 거절을할 경우 재요청이 가능함

```
POST /api/friends/request
Headers: X-user-Id (필수)
Body : {...}
Resp : 200 OK
```

### 4-4. 친구 수락

- 요청 시, 요청자와 수락자의 친구 목록에 관계가 추가됩니다.
- 본인의 설계에 따라 body는 임의로 필요한 내용을 채우면 됩니다.
- 수락 불가 상황일 경우 예외를 반환하며, 상황에 맞는 HTTP Status Code와 메시지를 내려야 합니다.

```
POST /api/friends/accept/{requestId}
Headers: X-user-Id (필수)
Body : {...}
Resp : 200 OK
```

### 4-5. 친구 거절

- 요청 시, 받은 요청 데이터가 삭제됩니다.
- 본인의 설계에 따라 body는 임의로 필요한 내용을 채우면 됩니다.
- 거절 불가 상황일 경우 예외를 반환하며, 상황에 맞는 HTTP Status Code와 메시지를 내려야 합니다.

```
POST /api/friends/reject/{requestId}
Headers: X-user-Id (필수)
Body : {...}
Resp : 200 OK
```

### 4-6. 기타

- 그 외 필요한 API는 자유롭게 추가할 수 있습니다.

## 📚 제출 요구사항

- `README.md`에 실행 방법과 핵심 로직 설명을 반드시 포함할 것
- 프로젝트에 데이터베이스 스키마 포함
- API 명세는 Swagger로 확인 가능하도록 구성
- 테스트 코드는 선택사항 (필수 아님)

## 📚 과제 제출

1. GitHub에 `apr-backend-assignment`라는 이름의 private 레포지토리를 생성합니다.
2. `aprdevelopmentorg`를 collaborator로 추가합니다.
3. 과제 부여일로부터 7일 이내, 편한 시간에 구현을 진행합니다.
4. `main` 이외의 브랜치에 커밋 후 PR을 작성하고, 최종적으로 `main`에 병합합니다.
5. 최종 완료된 레포지토리 주소를 제출합니다.

## 📖 (참고) 평가기준

| **항목** | **비중** |
| --- | --- |
| 기능 요구사항 | 40% |
| 비기능 요구사항 | 40% |
| 코드 품질/테스트 | 5% |
| 문서화 | 15% |