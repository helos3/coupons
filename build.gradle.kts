import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.gradle.jvm.tasks.Jar

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
val kotlin_version: String by extra
group = "coupons"
version = "1.0"

plugins {
    kotlin("jvm") version "1.2.70"
    application
}


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
    compile("org.jooby:jooby-gson:1.5.1")
    compile("org.jooby:jooby-apitool:1.5.1")
    compile("org.jooby:jooby-jdbc:1.5.1")
    compile("org.jooby:jooby-flyway:1.5.1")
    compile("org.jsoup:jsoup:1.10.3")
    compile("org.jetbrains.exposed:exposed:0.10.5")
    compile("org.postgresql:postgresql:42.2.2")
    compile("org.jooby:jooby-hbm:1.5.1")

}


task<Wrapper>("wrapper") {
    gradleVersion = "4.10"
    distributionType = DistributionType.ALL
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "org.fyde.AppKt"
    applicationName = "coupons"
    version = "1.0"
    group = "org.fyde"
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Main-Class"] = "org.fyde.AppKt"
    }
    from(
            configurations.runtime.map {
                if (it.isDirectory) it else zipTree(it)
            }
    )
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}

