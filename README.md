# Simple Flags Kotlin

Kotlin 및 Spring Boot 애플리케이션을 위한 경량 피처 플래그 라이브러리입니다.

## 개요

`simple-flags-kotlin`은 복잡한 설정이나 외부 솔루션 없이 기본적인 피처 플래그 기능을 애플리케이션에 쉽게 통합할 수 있도록 설계되었습니다.

**주요 목표:**

* **추가 배포 없이 기능 ON/OFF:** 문제가 발생한 기능을 코드 배포 없이 즉시 비활성화합니다.
* **그룹핑을 통한 안전한 기능 배포(향후 추가..):** 새로운 기능을 특정 사용자 그룹(ID, 이메일 도메인 등 사용자들을 식별할 수 있는 무언가..)에게만 점진적으로 노출합니다.
* **간단한 설정:** JSON 파일을 통해 플래그 규칙을 관리합니다.

## 요구사항

- Java 17 이상이 필요합니다.
- Spring Boot 3.x와 호환됩니다.

## 설치 방법

이 라이브러리는 JitPack을 통해 배포됩니다.

라이브러리 설치

```build.gradle.kts
repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/sksk713/simple-feature-flags")
        credentials {
            username = property("gpr.user")?.toString() ?: System.getenv("GITHUB_USERNAME")
            password = property("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
    
dependencies {
    implementation("com.github.sksk713:simple-flags-kotlin:1.0.0")
}
```

gpr.user과 gpr.key는 GitHub 패키지 레지스트리에서 발급받은 사용자 이름과 토큰입니다. 이 값들은 `gradle.properties` 파일에 저장하거나 환경 변수로 설정할 수 있습니다.
- gradle.properties은 프로젝트 root에 위치해야 합니다.

## 설정

1. **`feature_flags.json` 파일 생성:** 애플리케이션의 클래스패스 루트나 특정 경로에 `feature_flags.json` 파일을 생성합니다.
      ```json
          {
            "new-awesome-feature": true,
            "experimental-feature-a": false
          }
      ```

2. application.yml 파일에 다음과 같이 설정합니다.
    ```yaml
    simple:
      flags:
        location: file:/path/to/your/feature_flags.json
        dynamic-reloading-enabled: true
        reload-cron: "0 * * * * ?"
        reload-interval-seconds: 20
    ```
    - location은 반드시 지정이 필요하고, 그 외의 설정은 선택 사항입니다.

## 사용 방법

Spring Boot 애플리케이션에서 `FeatureFlagService` 빈을 주입받아 사용합니다.

```kotlin
import org.simplefeatureflags.service.FeatureFlagService
import org.springframework.stereotype.Service

@Service
class MyAwesomeService(
    private val featureFlagService: FeatureFlagService
) {

    fun doSomething() {
        if (featureFlagService.isEnabled("new-awesome-feature")) {
            ...
        } else {
            ...
        }
    }

    fun anotherMethod() {
        if (featureFlagService.isDisabled("experimental-feature-a")) {
            println("실험적 기능 a는 비활성화되어 있습니다.")
        }
    }
}
```