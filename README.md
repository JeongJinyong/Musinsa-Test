# Musinsa 상품 전시 화면

## 기술 스택

- **언어**: Kotlin
- **아키텍처**: MVI (Model-View-Intent) with Airbnb's Mavericks
- **UI**: Jetpack Compose
- **비동기 처리**: Coroutine
- **이미지 로딩**: Coil
- **네트워크**: Retrofit2
- **의존성 주입**: Koin

## 주요 기능

1. **다양한 콘텐츠 표시**
    - 배너, 상품, 스타일 등 다양한 형태의 콘텐츠 표시
    - 콘텐츠 타입에 따른 다양한 레이아웃 구현

2. **상호작용**
    - REFRESH: 콘텐츠를 랜덤하게 섞어 새로고침
    - MORE: 콘텐츠에 추가 행 표시

## 프로젝트 구조

```
com.aidan.musinsa/
├── data/
│   ├── model/          # 데이터 모델 클래스들
│   ├── remote/         # API 관련 클래스들 (Retrofit 인터페이스, 서비스 등)
│   └── repository/     # 데이터 접근 로직
├── di/                 # 의존성 주입 모듈
└── ui/
    ├── components/     # 재사용 가능한 UI 컴포넌트
    ├── feature/
    │   └── home/       # 홈 화면 관련 코드
    │       ├── HomeScreen.kt     # 메인 컴포저블
    │       ├── HomeState.kt      # 상태 관리 클래스
    │       └── HomeViewModel.kt  # 뷰모델
    └── theme/          # 앱 테마 정의
```

## 구현 내용

### HomeViewModel

콘텐츠 로드, 새로고침, 확장 기능을 구현했습니다:

- `loadContents()`: API를 통해 콘텐츠 목록을 가져와 상태 업데이트
- `refreshContents(contentIndex)`: 특정 콘텐츠의 아이템들을 랜덤하게 섞음
- `expandContent(contentIndex)`: 특정 콘텐츠에 추가 행 표시
- `onFooterClick(contentIndex)`: 푸터 타입에 따라 적절한 액션 수행

### 콘텐츠 타입

- **BANNER**: 스와이프 가능한 배너로 표시
- **GRID**: 3×2 Grid 형태로 콘텐츠 표시
- **SCROLL**: 횡스크롤 형태로 콘텐츠 표시
- **STYLE**: 첫 번째 아이템이 2x2 Span된 3열 Grid 형태로 표시

### 푸터 타입

- **REFRESH**: 콘텐츠 순서를 랜덤하게 바꾸어 갱신
- **MORE**: 현재 콘텐츠에 1행을 추가로 더 표시

## 테스트

JUnit4와 Mockk을 사용하여 단위 테스트를 구현했습니다:

- `HomeStateTest`: 상태 관리 테스트
- `HomeViewModelTest`: 뷰모델 기능 테스트