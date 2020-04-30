package io.toolebox.gradle.gitversioner

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.FreeSpec
import java.io.File
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner

class GitVersionerSpec : FreeSpec() {
    private val projectDirectory = File("build/tmp/functionalTest/GitVersionerSpec")

    override fun beforeTest(testCase: TestCase) {
        projectDirectory.deleteRecursively()
        File("src/functionalTest/resources/settings.gradle").copyTo(
            File(
                projectDirectory,
                "settings.gradle"
            )
        )
        super.beforeTest(testCase)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        projectDirectory.deleteRecursively()
        super.afterTest(testCase, result)
    }

    init {
        "Groovy" - {
            "Version is available after forcing version resolution" {
                `given the project is using a groovy build file`("configured")
                `given the project has a git repository with commits`()

                val result = `when I run the task`("printVersionEarly")

                result.output shouldContain "2.1.1.4"
            }
        }
        "Kotlin" - {
            "Version is available after forcing version resolution" {
                `given the project is using a kotlin build file`("configured")
                `given the project has a git repository with commits`()

                val result = `when I run the task`("printVersionEarly")

                result.output shouldContain "2.1.1.4"
            }
        }
    }

    private fun `given the project has a git repository with commits`() {
        val repo = Git.init().setDirectory(projectDirectory).call()
        commit(repo, "trex")
        commit(repo, "stego")
        commit(repo, "compy")
        commit(repo, "[major]")
        commit(repo, "[minor]")
        commit(repo, "[patch]")
        commit(repo, "message")
    }

    private fun commit(repo: Git, message: String) {
        repo.commit().setAllowEmpty(true).setMessage(message).call()
    }

    private fun `given the project is using a groovy build file`(name: String) {
        File("src/functionalTest/resources/$name-build.gradle").copyTo(
            File(
                projectDirectory,
                "build.gradle"
            )
        )
    }

    private fun `given the project is using a kotlin build file`(name: String) {
        File("src/functionalTest/resources/$name-build.gradle.kts").copyTo(
            File(
                projectDirectory,
                "build.gradle.kts"
            )
        )
    }

    private fun `when I run the task`(task: String) = GradleRunner.create()
        .withProjectDir(projectDirectory)
        .withArguments(task, "-q")
        .withPluginClasspath()
        .forwardOutput()
        .build()
}
