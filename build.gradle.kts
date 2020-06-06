import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.diffplug.gradle.spotless") version "3.28.1"
    id("com.gradle.plugin-publish") version "0.11.0"
    id("io.toolebox.git-versioner") version "1.4.0"
    `java-gradle-plugin`
    kotlin("jvm") version "1.3.72"
    `kotlin-dsl`
}

group = "io.toolebox"

setUpExtraTests("functional")
setUpExtraTests("integration")

repositories {
    mavenCentral()
}

dependencies {
    val kotlintestVersion = "4.0.6"

    compileOnly(gradleApi())
    implementation(kotlin("stdlib-jdk8", "1.3.70"))
    implementation(kotlin("reflect", "1.3.70"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.0.1.201806211838-r")

    testImplementation("junit:junit:4.12")
    testImplementation("io.kotest:kotest-core:$kotlintestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotlintestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotlintestVersion")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

pluginBundle {
    website = "https://github.com/toolebox-io/gradle-git-versioner"
    vcsUrl = "https://github.com/tooleboxio/gradle-git-versioner"
    tags = listOf("git", "version", "semantic-version")
}

gradlePlugin {
    plugins {
        create("versionerPlugin") {
            id = "io.toolebox.git-versioner"
            displayName = "Git Versioner Plugin"
            description = "A Gradle plugin to automatically version a project based on commit messages and semantic versioning principles"
            implementationClass = "io.toolebox.gradle.gitversioner.VersionerPlugin"
        }
    }
    testSourceSets(sourceSets["functionalTest"])
}

val setupPluginSecrets = tasks.create("setupPluginSecrets") {
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
                token = project.findProperty("token") as String?
            }
        }
    }
    tag {
        useCommitMessage = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun setUpExtraTests(type: String) {
    sourceSets.register("${type}Test") {
        compileClasspath += sourceSets["main"].output + configurations["testRuntime"]
        runtimeClasspath += sourceSets["main"].output
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("src/${type}Test/kotlin")
        }
    }

    configurations["${type}TestImplementation"].extendsFrom(configurations["testImplementation"])

    tasks.register("${type}Test", Test::class.java) {
        description = "Runs the $type tests"
        group = "verification"
        testClassesDirs = sourceSets["${type}Test"].output.classesDirs
        classpath = sourceSets["${type}Test"].runtimeClasspath
        dependsOn("test")
        tasks["check"].dependsOn(this)
    }
}
