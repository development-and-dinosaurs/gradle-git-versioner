import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.9.20"
    id("com.diffplug.spotless") version "6.23.3"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("io.toolebox.git-versioner") version "1.6.7"
    id("pl.droidsonroids.jacoco.testkit") version "1.0.12"
}

group = "uk.co.developmentanddinosaurs"

setUpExtraTests("functional")
setUpExtraTests("integration")

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.9.0.202009080501-r")
    implementation("com.jcraft:jsch:0.1.55")

    val kotestVersion = "4.3.1"
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

gradlePlugin {
    website = "https://github.com/developmentanddinosaurs/gradle-git-versioner"
    vcsUrl = "https://github.com/developmentanddinosaurs/gradle-git-versioner"
    plugins {
        create("versionerPlugin") {
            id = "uk.co.developmentanddinosaurs.git-versioner"
            displayName = "Git Versioner Plugin"
            description = "Automatically version a project based on commit messages and semantic versioning principles"
            implementationClass = "uk.co.developmentanddinosaurs.gradle.gitversioner.VersionerPlugin"
            tags = listOf("git", "version", "semantic-version")
        }
    }
    testSourceSets(sourceSets["functionalTest"])
}

val setupPluginSecrets =
    tasks.create("setupPluginSecrets") {
        doLast {
            System.setProperty("gradle.publish.key", System.getenv("GRADLE_PUBLISH_KEY"))
            System.setProperty("gradle.publish.secret", System.getenv("GRADLE_PUBLISH_SECRET"))
        }
    }
tasks["publishPlugins"].dependsOn(setupPluginSecrets)

spotless {
    kotlin {
        target("**/*.kt")
        ktlint()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

versioner {
    git {
        authentication {
            https {
                token = System.getenv("TOKEN")
            }
        }
    }
    tag {
        useCommitMessage = true
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    executionData(fileTree(projectDir).include("build/jacoco/*.exec"))
    reports {
        xml.required = true
    }
}

jacocoTestKit {
    applyTo("functionalTestRuntimeOnly", tasks.named("functionalTest"))
}

fun setUpExtraTests(type: String) {
    sourceSets.register("${type}Test") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }

    configurations["${type}TestImplementation"].extendsFrom(configurations["testImplementation"])

    tasks.register("${type}Test", Test::class.java) {
        doNotTrackState("jacoco")
        description = "Runs the $type tests"
        group = "verification"
        testClassesDirs = sourceSets["${type}Test"].output.classesDirs
        classpath = sourceSets["${type}Test"].runtimeClasspath
        dependsOn("test")
        tasks["check"].dependsOn(this)
    }
}
