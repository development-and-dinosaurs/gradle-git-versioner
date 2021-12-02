import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.diffplug.spotless") version "5.7.0"
    id("com.gradle.plugin-publish") version "0.12.0"
    id("io.toolebox.git-versioner") version "1.4.0"
    id("org.sonarqube") version "3.0"
    id("pl.droidsonroids.jacoco.testkit") version "1.0.7"
    jacoco
    `java-gradle-plugin`
    kotlin("jvm") version "1.4.10"
    `kotlin-dsl`
}

group = "io.toolebox"

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
            description = "Automatically version a project based on commit messages and semantic versioning principles"
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
                token = System.getenv("TOKEN")
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
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    executionData(fileTree(projectDir).include("build/jacoco/*.exec"))
    reports {
        xml.isEnabled = true
    }
}

jacocoTestKit {
    applyTo("functionalTestRuntimeOnly", tasks.named("functionalTest"))
}

sonarqube {
    properties {
        property("sonar.projectKey", "toolebox-io_gradle-git-versioner")
        property("sonar.organization", "toolebox-io")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_LOGIN"))
        property("sonar.sources", "src/main/kotlin")
        property("sonar.tests", "src/test/kotlin,src/integrationTest/kotlin,src/functionalTest/kotlin")
        property("sonar.junit.reportPaths", "build/test-results/**/*.xml")
        property("sonar.jacoco.reportPaths", "build/jacoco/*.exec")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.cpd.exclusions", "src/test/**/*,src/integrationTest/**/*,src/functionalTest/**/*")
    }
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

tasks.withType<AbstractArchiveTask>() {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
