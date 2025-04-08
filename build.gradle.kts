plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "2.1.20"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.12.0"
}

group = "ru.ershov.ddd.delivery"
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
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    implementation("org.postgresql:postgresql")
    implementation("org.testcontainers:postgresql")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
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

tasks {
    compileKotlin {
        dependsOn("openApiGenerate")
    }
}

sourceSets {
    main {
        java {
            srcDir("${project.buildDir}/generated/openapi")
        }
    }
}

openApiGenerate {

    outputDir = "${project.buildDir}/generated/openapi"
    generatorName.set("kotlin-spring")
    inputSpec.set("${project.rootDir}/src/main/resources/openapi/openapi.yaml")
    packageName.set("$group.api.adapters.generated")
    invokerPackage.set("$group.api.adapters.generated")
    configOptions.set(mapOf(
        "useTags" to "true",          // Генерировать контроллеры по тегам из OpenAPI
        "interfaceOnly" to "true",     // Только интерфейсы (без имплементаций)
        "skipDefaultInterface" to "true",
        "useSpringBoot3" to "true",    // Для Spring Boot 3.x
        "openApiNullable" to "false",  // Отключить поддержку nullable
        "useBeanValidation" to "true"  // Включить валидацию (@Valid, @NotNull и т.д.)
    ))
    ignoreFileOverride.set("${project.rootDir}/src/main/resources/openapi/.openapi-generator-ignore")

}
