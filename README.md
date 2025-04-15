# Simple Flags Kotlin
Kotlin 및 Spring Boot 애플리케이션을 위한 경량 피처 플래그 라이브러리입니다.

## 개요
`simple-flags-kotlin`은 복잡한 설정이나 외부 솔루션 없이 기본적인 피처 플래그 기능을 애플리케이션에 쉽게 통합할 수 있도록 설계되었습니다.

**주요 목표:**
* **추가 배포 없이 기능 ON/OFF:** 문제가 발생한 기능을 코드 배포 없이 즉시 비활성화합니다.
* **그룹핑을 통한 안전한 기능 배포:** 새로운 기능을 특정 사용자 그룹(ID, 이메일 도메인 등 사용자들을 식별할 수 있는 무언가..)에게만 점진적으로 노출합니다.
* **간단한 설정:** JSON 파일을 통해 플래그 규칙을 관리합니다.

## 요구사항
- Java 17 이상이 필요합니다.
- Spring Boot 3.x와 호환됩니다.

## 설치 방법
이 라이브러리는 JitPack을 통해 배포됩니다.

1.  **JitPack 저장소 추가:** 프로젝트의 루트 `settings.gradle.kts` 또는 루트 `build.gradle.kts` 파일에 JitPack 저장소를 추가합니다.
    ```kotlin
    // build.gradle.kts
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // JitPack 저장소 추가
    }
    ```

2.  **의존성 추가:** 사용하려는 모듈의 `build.gradle.kts` 파일에 다음 의존성을 추가합니다.
    ```kotlin
    dependencies {
        implementation("com.github.sksk713:simple-feature-flags:1.0.0") // 최신 버전으로 변경 필요
    }
    ```

## 설정

1.  **`feature_flags.json` 파일 생성:** 애플리케이션의 클래스패스 루트나 특정 경로에 `feature_flags.json` 파일을 생성합니다.

2.  **플래그 규칙 정의:**
    JSON 파일에 피처 플래그와 그룹핑 규칙을 정의합니다.
    ```
    추후 기능 개발 후, 추가 예정
    ```

## 사용 방법
추후 작성
