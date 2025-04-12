plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
}

val libraryArtifactId = "simple-flags-kotlin"
val libraryVersion = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot 의존성 버전 관리를 위한 BOM
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.3.10"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // 테스트 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Maven 설정
publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = libraryArtifactId
            version = libraryVersion
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

