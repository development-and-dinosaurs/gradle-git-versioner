package io.toolebox.gradle.gitversioner

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.StringSpec
import io.toolebox.gradle.gitversioner.util.Gradle
import io.toolebox.gradle.gitversioner.util.Project
import java.io.File

class GitVersionerSpec : StringSpec() {

    private val directory = File("build/tmp/functionalTest/GitVersionerSpec")
    private var project = Project.createProject(directory)
    private var gradle = Gradle(directory)

    override fun afterTest(testCase: TestCase, result: TestResult) {
        directory.deleteRecursively()
        super.afterTest(testCase, result)
    }

    init {
        "Version is available after forcing version resolution in Groovy" {
            project.withSettingsFile().withGit().withGroovyGradleFile("configured")
            addCommits(project)

            val result = gradle.runTask("printVersionEarly")

            result.output shouldContain "2.1.1.4"
        }
        "Version is available after forcing version resolution in Kotlin" {
            project.withSettingsFile().withGit().withKotlinGradleFile("configured")
            addCommits(project)

            val result = gradle.runTask("printVersionEarly")

            result.output shouldContain "2.1.1.4"
        }
    }

    private fun addCommits(project: Project) {
        project
            .withCommit("trex")
            .withCommit("stego")
            .withCommit("compy")
            .withCommit("[major]")
            .withCommit("[minor]")
            .withCommit("[patch]")
            .withCommit("message")
    }
}
