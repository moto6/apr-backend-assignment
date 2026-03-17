# 에이피알 백엔드 직무 과제

- 기능 요구사항
    - [x] 친구 신청 API: 타 사용자에게 요청을 보내는 기능
        - [x] 처리율 제한: 최대 친구 수 10,000명 초과 금지 정책 및 검증 로직 적용
    - [x] 친구 승인 API : 수락 시에만 최종 친구 관계 형성 처리
    - [x] 친구 거절 API : 거절 시 데이터 상태 반영 및 관계 미성립 처리
    - [x] 목록 조회 API: 현재 맺어진 친구 목록 조회(Pagination 적용) 개발
- 비 기능적 요구사항
    - [x] DB 스키마: 프로젝트 내 schema.sql 및 data.sql 포함 완료
    - [x] API 명세: Swagger(OpenAPI 3.0)를 통한 인터랙티브 문서화 완료[x] 테스트 코드: FriendValidatorTest 등 주요 로직 단위 테스트 작성 완료
    - [x] 테스트코드 : 검증이 필요한 핵심 기능에 테스트코드 작성

---

## 실행방법

- [1] 환경 설정
    - 프로젝트 루트 경로에서 명령어 입력 : `make setup`
        - Docker 기반으로 동작하며, DockerDesktop 이 동작중이여야 합니다
      - 실행하려는 컴퓨터에 8080 포트를 점유 가능한 상황에서만 실행가능합니다
- [2] 간단 테스트 실행
    - 프로젝트 루트 경로에서 명령어 입력 : `make test`
        - API 요구사항을 테스트합니다
        - 데이터/서버상태 초기화 명령어 : `make reset`
      - 처리율 제한 기능 구현 테스트 명령어 : `make many-request`
- [3] 종료 및 환경설정 삭제
    - 프로젝트 루트 경로에서 명령어 입력 : `make clean`
- [4] 이외
    - DB 스키마 : `src/main/resources/schema.sql` 파일
    - API 명세 : 웹서버 실행(`make setup`) 이후, `http://localhost:8080/swagger-ui/index.html` 
---

## 핵심로직 설명

### Server-Side Rate Limiter 구현

- 식별자 전략: 헤더, IP 기반 우선순위 식별자 인식
    - X-User-Id 헤더 기반 식별을 우선하며, 인증되지 않은 요청은 IP 주소를 추적 합니다
    - 각 키에 CID:, IP: 등 네임스페이스를 부여하여 식별자 간 충돌을 방지 합니다
- 알고리즘 설계 :  Lazy-Refill 기반 토큰 버킷 알고리즘을 사용
    - 별도의 스케줄러 없이 요청 시점에만 시간 차분(Delta)을 계산 합니다
    - 델타분 기반 토큰을 충전함으로써 불필요한 백그라운드 스레드 비용을 제거 합니다
- 구현 핵심 기술 : Interceptor 선정
    - Spring Interceptor를 채택하여 컨트롤러 진입 전 API 수준의 게이트웨이를 구축 합니다
    - Filter와 달리 HandlerMethod에 접근이 용이해, 메서드별 @RateLimit 어노테이션을 읽어 유연한 정책을 적용하기에 가장 적합한 계층이라 판단했습니다
- 동시성 및 성능 : 고성능 인메모리 처리 전략
    - ConcurrentHashMap 저장소와 버킷 단위 동기화(Synchronization)를 조합했습니다
    - 멀티스레드 환경에서도 데이터 정합성을 유지해주는 인메모리 컬렉션을 사용하였습니다
- 예외 및 응답 규격: 429 Too Many Requests 처리방식
    - 임계치 초과 시 HTTP 429 코드와 명확한 에러 메시지를 반환합니다
    - 최종 산출물 기반의 테스트를 지원합니다 (README.md 파일의 `make many-request` 항목 참고)
    - 또한, 저장소 레이어를 RateLimitBuckets 인터페이스로 추상화하여 DIP원칙을 준수하도록 설계했습니다

### 비지니스 로직 중 "친구는 10000 명을 넘을 수 없다" 정책 관리 전략

- 비즈니스 정책: 친구 수 상한선 제어 (Max Limit)
    - 친구 요청 수락 시점에 현재 친구 수를 조회하여 상한선 초과 여부를 엄격하게 검증합니다
- 검증 레이어 분리: 전용 Validator 객체 활용
    - FriendService의 복잡도를 낮추기 위해 검증 로직을 FriendValidator로 분리했습니다
    - 서비스 레이어는 비즈니스 흐름(Flow)에만 집중하고, 도메인 제약 조건은 전문 컴포넌트가 담당합니다
- 유연한 설정 관리: ConfigurationProperties 기반 정책 외부화
    - 상한선 수치(10,000)를 코드에 지정하지 않고 app.friend.max-limit 설정을 통해 관리합니다
    - @ConfigurationProperties를 활용하여 정책 변경 시 코드 수정 없이 환경 변수나 설정 파일 수정만으로 실시간 대응이 가능합니다
- 검증 가능한 비즈니스 로직: 경계값 테스트(Boundary Test) 수행
    - FriendValidatorTest를 통해 상한선 직전(9,999명)의 성공 케이스와 상한선 도달(10,000명) 시의 예외 발생 케이스를 검증합니다
- 예외 처리 정책: 의미 있는 에러 반환
    - 단순 JDK Default 에러가 아닌, 정책 위반임을 명확히 알리는 FriendLimitExceededException을 발생시켜 최종 API 응답에서 상황에 맞는 상태 코드와 메시지를 클라이언트에게
      제공합니다