import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.script.experimental.api.ScriptCompileConfigurationProperties.dependencies

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.50"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.9.10"
    id("io.toolebox.git-versioner") version "1.0.0"
}

group = "io.toolebox"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation(kotlin("stdlib-jdk8", "1.2.50"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.0.1.201806211838-r")

    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.10.0")
    testImplementation("io.kotlintest:kotlintest-core:3.1.7")
    testImplementation("io.kotlintest:kotlintest-assertions:3.1.7")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.7")
}

pluginBundle {
    website = "https://github.com/toolebox-io/gradle-git-versioner"
    vcsUrl = "https://github.com/toolebox-io/gradle-git-versioner"

    (plugins) {
        "versionerPlugin" {
            id = "io.toolebox.git-versioner"
            displayName = "Git Versioner Plugin"
            description = "A Gradle plugin to automatically version a project based on commit messages and semantic versioning principles"
            tags = listOf("git", "version", "semantic-version")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
