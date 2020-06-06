package io.toolebox.gradle.gitversioner.tasks

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.FreeSpec
import io.kotlintest.specs.StringSpec
import io.toolebox.gradle.gitversioner.util.Gradle
import io.toolebox.gradle.gitversioner.util.Project
import java.io.File
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner

class PrintVersionTaskSpec : StringSpec() {

    private val directory = File("build/tmp/functionalTest/GitVersionerSpec")
    private var project = Project.createProject(directory)
    private var gradle = Gradle(directory)

    override fun afterTest(testCase: TestCase, result: TestResult) {
        directory.deleteRecursively()
        super.afterTest(testCase, result)
    }

    init {
        "prints version using default configuration when none is supplied in Groovy" {
            project.withSettingsFile().withGit().withGroovyGradleFile("default")
            addCommits(project)

            val result = gradle.runTask("printVersion")

            result.output shouldContain "1.1.1.1"
        }
        "prints version using provided configuration when supplied in Groovy" {
            project.withSettingsFile().withGit().withGroovyGradleFile("configured")
            addCommits(project)

            val result = gradle.runTask("printVersion")

            result.output shouldContain "2.1.1.4"
        }
        "prints version using default configuration when none is supplied in Kotlin" {
            project.withSettingsFile().withGit().withKotlinGradleFile("default")
            addCommits(project)

            val result = gradle.runTask("printVersion")

            result.output shouldContain "1.1.1.1"
        }
        "prints version using provided configuration when supplied in Kotlin" {
            project.withSettingsFile().withGit().withKotlinGradleFile("configured")
            addCommits(project)

            val result = gradle.runTask("printVersion")

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
