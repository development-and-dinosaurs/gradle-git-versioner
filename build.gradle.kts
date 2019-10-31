import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.0"
    id("io.toolebox.git-versioner") version "1.1.1"
}

group = "io.toolebox"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation(kotlin("stdlib-jdk8", "1.3.50"))
    implementation(kotlin("reflect", "1.3.50"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.0.1.201806211838-r")

    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.10.0")
    testImplementation("io.kotlintest:kotlintest-core:3.1.7")
    testImplementation("io.kotlintest:kotlintest-assertions:3.1.7")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.7")
}

val setUpEnvironmentTask = tasks.create("setUpEnvironment") {
    doFirst {
        System.setProperty("gradle.publish.key", System.getenv("GRADLE_PUBLISH_KEY"))
        System.setProperty("gradle.publish.secret", System.getenv("GRADLE_PUBLISH_SECRET"))
    }
}

tasks.getByName("publishPlugins").dependsOn(setUpEnvironmentTask)

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
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
