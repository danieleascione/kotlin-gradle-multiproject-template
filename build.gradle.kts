apply(from = "versions.gradle.kts")

val kotlinVersion: String by extra
val junitVersion: String by extra
val jetBrainsAnnotationsVersion: String by extra
val assertjVersion: String by extra
val mockitoVersion: String by extra

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
    }
}

plugins {
    base
    `kotlin-dsl`
    kotlin("jvm") version "1.4.20" apply false
}

allprojects {
    group = "com.group"
    version = "1.0"

    repositories {
        jcenter()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    sourceSets {
        create("integrationTest") {
            java.srcDir("src/integrationTest/java")
            resources.srcDir("src/integrationTest/resources")
            compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
            runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
        }
    }

    val integrationTest = task<Test>("integrationTest") {
        description = "Runs integration tests."
        group = "verification"

        testClassesDirs = sourceSets["integrationTest"].output.classesDirs
        classpath = sourceSets["integrationTest"].runtimeClasspath
        shouldRunAfter("test")
    }

    tasks.check { dependsOn(integrationTest) }

    dependencies {
        implementation("org.jetbrains:annotations:$jetBrainsAnnotationsVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
        testImplementation("org.assertj:assertj-core:$assertjVersion")
    }
}
