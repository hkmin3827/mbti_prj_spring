# 기능명세서 (Functional Specification)

프로젝트명: **MBTI 기반 데이트 장소 추천 플랫폼**

버전: v1.0  

프로젝트 기간: 25.12.10 ~ 26.01.14

개발 형태: 개인 프로젝트 (기획 · 설계 · 개발 전담)

---

## 1. 프로젝트 개요

### 1.1 목적
사용자의 **MBTI(본인/상대)** 및 **행동 로그(조회, 좋아요, 북마크)** 를 기반으로  
개인화된 데이트 장소를 추천하는 플랫폼을 구현한다.

단순한 조건 필터링이 아닌,
추정 성향 + 행동 누적에 따라 추천 점수가 동적으로 변하는 구조를 목표로 한다.

외부 지도 API를 통해 장소 후보군을 수집하고,  
내부 키워드 및 가중치 도메인을 통해 추천 품질을 고도화한다.

### 1.2 대상 사용자
- 데이트 장소를 추천받고 싶은 사용자
- MBTI 성향 기반 추천을 선호하는 사용자

### 1.3-1 기존 장소 추천 서비스의 한계
- 키워드 검색어 기반의 단순 추천
- 추천결과의 근거를 구조적으로 설명하기 어려운 알고리즘 기반

### 1.3-2 해결 방향
- MBTI를 E/I, S/N, T/F, J/P 축 단위로 분해
- 장소의 성격을 키워드 도메인으로 정규화
- 사용자 행동을 가중치로 환산하여 추천 점수에 누적 반영

---

## 2. 기술 스택

### Backend
- Spring Boot
- Spring Security (OAuth2, JWT)
- Spring Data JPA
- MySQL

### External / Infra
- Kakao Map API
- AWS S3 (Presigned URL)
- GOOGLE CLOUD VISION (OCR)
---

## 3. 도메인 모델 요약

### 핵심 엔티티
- **User**: 사용자, MBTI 정보 포함
- **Place**: 장소
- **Keyword**: 정규화된 키워드
- **PlaceKeyword**: 장소-키워드 연결 및 가중치
- **MbtiKeywordWeight**: MBTI 축 기반 키워드 가중치
- **UserKeywordPreference**: 사용자 키워드 선호 점수
- **PlaceReaction**: 좋아요와 같은 반응 표현 수단
- **PlaceBookmark**: 장소 저장
- **PlaceViewHistory**: 최근 조회한 장소
- **Review**: OCR 기반 리뷰 (선택 기능)
- **UserActionLog**: 사용자 행동 로그

### 주요 개념
- **MbtiAxis**: MBTI를 E/I, S/N, T/F, J/P 축으로 분해
- **MbtiContext**: 내 MBTI / 상대 MBTI 기준 추천 분리
- **ActionType**: LIKE, VIEW, CLICK 등

---

## 4. 기능 목록 요약

| ID   | 기능명 | 설명 | 우선순위 |
|------|---|---|---|
| F-01 | 인증/인가 | OAuth2 + JWT 로그인 | 필수 |
| F-02 | 장소 검색 | 위치 기반 장소 조회 | 필수 |
| F-03 | 장소 추천 | MBTI + 행동 기반 추천 | 필수 |
| F-04 | 반응 | 좋아요 등 사용자 반응 | 필수 |
| F-05 | 북마크 | 장소 저장 기능 | 필수 |
| F-06 | 리뷰 | OCR + 이미지 업로드 | 선택 |

---

## 5. 기능 상세 명세

## 5.1 F-01 인증 / 인가

### 설명
- OAuth2 기반 로그인 제공
- 로그인 성공 시 JWT 발급
- 인증이 필요한 API는 토큰 없을 시 접근 불가

### 처리 규칙
- 신규 사용자: 자동 회원가입
- 기존 사용자: provider + oauthId 기준 조회

### 예외
- 인증 실패: `401 Unauthorized`

---

## 5.2 F-02 장소 검색

### 설명
사용자는 특정 위치와 반경을 기준으로 장소를 검색할 수 있다.

### 입력
- 위도(lat)
- 경도(lng)
- 반경(radius)
- 카테고리(category)

### 출력
- 장소 후보 리스트

### 처리 규칙
- Kakao Map API를 통해 장소 후보군 조회
- 중복 장소는 제거
- 첫 클릭 발생 시 내부 Place 엔티티로 저장

---

## 5.3 F-03 장소 추천

### 설명
MBTI 축 가중치와 사용자 행동 데이터를 기반으로 장소를 추천한다.

### 입력
- 사용자 ID
- 타겟 MBTI

### 출력
- 추천 장소 리스트 (점수 포함)

### 점수 계산 로직
1. 타겟 MBTI를 축(MbtiAxis) 단위로 분해
2. 축별 키워드 가중치(MbtiKeywordWeight) 로드
3. 장소가 가진 키워드와 매칭하여 기본 점수 산출
4. 사용자 행동 가중치 누적 반영

### 비기능 요구사항
- 추천 계산은 **순수 계산기 클래스**에서 수행
- DB 접근 로직과 계산 로직 분리
- 서비스 레이어는 데이터 조립만 담당

---

## 5.4 F-04 장소 반응 (좋아요)

### 설명
사용자는 장소에 대해 좋아요, 싫어요와 같은 반응을 남길 수 있다.

### 입력
- 장소 ID
- ActionType
- MbtiContext

### 처리 규칙
- 동일 Mbti Context + 장소 + 타입에 대해 최신 반응만 유효
- 추천 점수 계산 시 행동 가중치로 활용

---

## 5.5 F-05 장소 북마크

### 설명
사용자는 장소를 저장(북마크)할 수 있다.

### 입력
- 장소 ID
- MbtiContext (저장은 User 별로 1번. 메타정보로 context 받음)

### 처리 규칙
- 중복 저장 방지
- 토글 방식 또는 명시적 삭제 방식 중 하나 채택

---

## 5.6 F-06 리뷰 생성 (선택)

### 설명
OCR 결과와 이미지 업로드를 통해 리뷰를 생성한다.
OCR 결과 score가 verified 되면 "인증됨"을 부여함

### 처리 흐름
1. Presigned URL 발급
2. 클라이언트 → S3 이미지 업로드
3. OCR 처리
4. 리뷰 엔티티 생성

---

## 6. 예외 처리 정책

| 상황 | 응답 |
|---|---|
| 인증 실패 | 401 |
| 권한 없음 | 403 |
| 리소스 없음 | 404 |
| 검증 실패 | 400 |
| 서버 오류 | 500 |

---

## 7. 비기능 요구사항

- 추천 계산 로직 분리
- 외부 API 호출 최소화
- AI 엔진 확장 가능한 구조 유지

---

## 8. 향후 확장 계획

- 추천 결과 설명 기능 (왜 이 장소가 추천되었는지)
- 행동 가중치 자동 학습
- 통계 대시보드
- AI 기반 장소 키워드 자동 보강

---

## Testing Strategy

본 프로젝트는 개인 프로젝트로서 모든 계층에 대한 전면 테스트보다는,
추천 결과의 신뢰성에 직접적인 영향을 주는 핵심 비즈니스 로직 위주로
선택적 테스트를 수행했습니다.

특히 MBTI 기본 가중치와 유저 행동 가중치가
어떻게 결합되어 추천 점수에 반영되는지를
서비스 레벨에서 검증하는 데 초점을 두었습니다.

---

## 보완해야하는 사항
MBTI 프로젝트의 코드 개선점들을 파악하여, 이후의 발전을 위해 기록.

- src/.../application/user/AuthService.java : withdraw 트랜잭션 어노테이션 제거 또는 외부 API 트랜잭션 분리
- src/.../global/security/oauth/userInfo/GoogleOAuthUserInfo.java, NaverOAuthUserInfo.java... : **providerId(필수값) Null 방어** 커스텀 예외 처리로 ErrorCode 추가, OAuth핸들러에서 onAuthenticationSuccess()에서 try-catch 필요 -> frontBaseUrl + "/oauth/callback?error=" + e.getErrorCode().name() 로 리다이렉트 처리
- src/.../infra/kakao/KakaoMapClient.java : existsPlace 현재 폐업 시라도 결과 0 건으로, Exception이 떨어지지 않음. 카카오 공식에서 placeId로 검색하는 api 제공하지 않으므로, 장소명 + 주소 등으로 keword검색 후 0건인지 확인 후 sync에서 softdelete. 또는 우회법으로 웹페이지에 GET 요청 보내 404 여부 판별하는 방식으로 해야 함.

