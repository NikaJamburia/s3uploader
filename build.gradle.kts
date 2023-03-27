import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.9"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("com.github.johnrengelman.shadow") version "7.0.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

repositories {
	mavenCentral()
}

allprojects {

	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")

	group = "ge.nika"
	version = "0.0.1-SNAPSHOT"
	java.sourceCompatibility = JavaVersion.VERSION_11

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.7.9")

		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

		implementation("com.amazonaws:aws-java-sdk-s3:1.12.418")

		testImplementation("io.mockk:mockk:1.12.0")
		testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
		testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.9")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
