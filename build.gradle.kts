plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.cloud.tools.jib") version "3.4.5"
}

group = "com.depromeet"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

	// 메일 발송
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")

	val jjwtVersion = "0.12.5"
	implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
	implementation("com.nimbusds:nimbus-jose-jwt:9.37.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

jib {
	val imageName: String = project.name
	val imageTag: String = System.getenv("GITHUB_SHA") ?: "dev"
	val dockerUser: String = System.getenv("DOCKER_USER") ?: "ddingmin00"

	from {
		image = "amazoncorretto:21-alpine3.19-jdk"
		platforms {
			platform {
				architecture = "arm64"
				os = "linux"
			}
		}
	}
	to {
		image = "$dockerUser/$imageName"
		tags = setOf("latest", imageTag)
	}
	container {
		creationTime = "USE_CURRENT_TIMESTAMP"
		ports = listOf("8080")
		environment = mapOf("timezone" to "Asia/Seoul")
		user = "1000:1000"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
