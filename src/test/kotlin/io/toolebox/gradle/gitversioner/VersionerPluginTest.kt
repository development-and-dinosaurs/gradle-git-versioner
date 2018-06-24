package io.toolebox.gradle.gitversioner

import io.kotlintest.specs.FreeSpec
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.io.PrintWriter

class VersionerPluginTest : FreeSpec() {

    private lateinit var projectDir: File
    private lateinit var buildFile: File
    private lateinit var project: Project

    init {
        "Versioner Plugin" - {
            "Increments major version for commit messages matching major regex" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasMajorCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("3.0.0")
            }
            "Increments minor version for commit messages matching minor regex" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasMinorCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("0.3.0")
            }
            "Increments minor version for commit messages matching patch regex" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasPatchCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("0.0.3")
            }
            "Increments build version for commit messages not matching any regex" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasRegularCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("0.0.0.3")
            }
            "Major version increment resets minor, patch, and build versions" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasMinorCommitsNumbering(3)
                givenRepositoryHasPatchCommitsNumbering(3)
                givenRepositoryHasRegularCommitsNumbering(3)
                givenRepositoryHasMajorCommitsNumbering(1)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("1.0.0")
            }
            "Minor version increment resets patch and build versions" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasPatchCommitsNumbering(3)
                givenRepositoryHasRegularCommitsNumbering(3)
                givenRepositoryHasMinorCommitsNumbering(1)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("0.1.0")
            }
            "Patch version increment resets build version" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasRegularCommitsNumbering(3)
                givenRepositoryHasPatchCommitsNumbering(1)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("0.0.1")
            }
            "Build version increment resets nothing" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasMajorCommitsNumbering(1)
                givenRepositoryHasMinorCommitsNumbering(1)
                givenRepositoryHasPatchCommitsNumbering(1)
                givenRepositoryHasRegularCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("1.1.1.3")
            }
            "Works even when there's loads of commits" {
                givenProjectIsUsingDefaultConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasRegularCommitsNumbering(100)
                givenRepositoryHasPatchCommitsNumbering(3)
                givenRepositoryHasMinorCommitsNumbering(1)
                givenRepositoryHasPatchCommitsNumbering(10)
                givenRepositoryHasMajorCommitsNumbering(1)
                givenRepositoryHasPatchCommitsNumbering(100)
                givenRepositoryHasMinorCommitsNumbering(13)
                givenRepositoryHasPatchCommitsNumbering(10)
                givenRepositoryHasRegularCommitsNumbering(10)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("1.13.10.10")
            }
            "Increments major version from starting point specified in configuration" {
                givenProjectIsUsingCustomConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasMajorCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("4.0.0")
            }
            "Increments minor version from starting point specified in configuration" {
                givenProjectIsUsingCustomConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasMinorCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("1.4.0")
            }
            "Increments patch version from starting point specified in configuration" {
                givenProjectIsUsingCustomConfiguration()
                givenProjectContainsGitRepository()
                givenRepositoryHasPatchCommitsNumbering(3)

                val result = runTask()

                assertThat(result.output).isEqualToIgnoringNewLines("1.1.4")
            }
        }
    }

    private fun givenProjectIsUsingDefaultConfiguration() {
        projectDir = createTempDir()
        buildFile = File("$projectDir/build.gradle")
                .withContents(this::class.java.getResourceAsStream("/default-build.gradle")
                        .readBytes())
        project = ProjectBuilder().withProjectDir(projectDir).build()
    }

    private fun givenProjectIsUsingCustomConfiguration() {
        projectDir = createTempDir()
        buildFile = File("$projectDir/build.gradle")
                .withContents(this::class.java.getResourceAsStream("/configured-build.gradle")
                        .readBytes())
        project = ProjectBuilder().withProjectDir(projectDir).build()
    }

    private fun givenProjectContainsGitRepository() {
        project.exec {
            it.commandLine("git")
            it.args("init")
        }
    }

    private fun givenRepositoryHasMajorCommitsNumbering(number: Int) {
        createCommits("[major]", number)
    }

    private fun givenRepositoryHasMinorCommitsNumbering(number: Int) {
        createCommits("[minor]", number)
    }

    private fun givenRepositoryHasPatchCommitsNumbering(number: Int) {
        createCommits("[patch]", number)
    }

    private fun givenRepositoryHasRegularCommitsNumbering(number: Int) {
        createCommits("Hello", number)
    }

    private fun createCommits(message: String, number: Int) {
        for (i in 1..number) {
            project.exec {
                it.commandLine("git")
                it.args("commit", "-m", "Commit$i\n\n$message", "--allow-empty")
            }
        }
    }

    private fun runTask(): BuildResult {
        return GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("-q", "printVersion")
                .forwardStdOutput(PrintWriter(System.out))
                .build()
    }
}
