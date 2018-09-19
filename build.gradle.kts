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
    maven {
        url = uri("https://dl.bintray.com/kotlin/exposed")
    }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.jooby:jooby-lang-kotlin:1.5.1")
    compile("org.jooby:jooby-netty:1.5.1")
    compile("org.jooby:jooby-jackson:1.5.1")
    compile("org.jooby:jooby-apitool:1.5.1")
    compile("org.jooby:jooby-jdbc:1.5.1")
    compile("org.jsoup:jsoup:1.10.3")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4")
    compile("org.jetbrains.exposed:exposed:0.10.5")
    compile("postgresql:postgresql:9.1-901-1.jdbc4")

}
task<Wrapper>("wrapper") {
    gradleVersion = "4.10"
    distributionType = DistributionType.ALL
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}