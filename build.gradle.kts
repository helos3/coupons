import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.61"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
}

group = "coupons"
version = "1.0"

apply {
    plugin("kotlin")
}

val kotlin_version: String by extra

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("org.jooby:jooby-lang-kotlin:1.5.1")
    compile("org.jooby:jooby-netty:1.5.1")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4")
    compile("org.jooby:jooby-jackson:1.5.1")
}

task("wrapper") {
    version = "4.7"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}