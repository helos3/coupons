import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.61"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

group = "coupons"
version = "1.0"

plugins {
    kotlin("jvm") version "1.2.70"
}

val kotlin_version: String by extra

repositories {
    mavenCentral()
    maven { artifactUrls("https://jitpack.io") }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.jooby:jooby-lang-kotlin:1.5.1")
    compile("org.jooby:jooby-netty:1.5.1")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4")
    compile("org.jooby:jooby-jackson:1.5.1")
    compile("org.jooby:jooby-apitool:1.5.1")
    compile("com.github.vjames19.kotlin-futures:kotlin-futures-jdk8:1.2.0")
}
task<Wrapper>("wrapper") {
    gradleVersion = "4.10"
    distributionType = DistributionType.ALL
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}