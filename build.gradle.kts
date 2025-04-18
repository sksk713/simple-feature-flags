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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.slf4j:slf4j-api:2.0.17")

    // 테스트 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
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
        maven {
            name = "GitHubPackages"
            val repositoryOwner = System.getenv("GITHUB_REPOSITORY_OWNER") ?: "YOUR_GITHUB_USERNAME_OR_ORG"
            url = uri("https://maven.pkg.github.com/$repositoryOwner/${rootProject.name}")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        mavenLocal()
    }
}

