package io.toolebox.gradle.gitversioner.tag

import io.kotlintest.specs.FreeSpec
import io.toolebox.gradle.gitversioner.withContents
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter

class GitTaggerSpec : FreeSpec() {

    private lateinit var localProjectDir: File
    private lateinit var remoteProjectDir: File
    private lateinit var buildFile: File
    private lateinit var localProject: Project
    private lateinit var remoteProject: Project

    init {
        "Git Tagger" - {
            "Creates local tag and pushes to remote repository with default prefix" {
                givenWeHaveLocalAndRemoteRepositories()
                givenProjectIsUsingDefaultConfiguration()
                givenRepositoryHasRegularCommitsNumbering(3)

                runTask()

                val output = ByteArrayOutputStream()
                remoteProject.exec {
                    it.standardOutput = output
                    it.errorOutput = output
                    it.commandLine("git")
                    it.args("tag")
                }
                assertThat(output.toString()).isEqualToIgnoringNewLines("v0.0.0.3")
            }
            "Creates tag with specified prefix when configured" {
                givenWeHaveLocalAndRemoteRepositories()
                givenProjectIsUsingCustomConfiguration()
                givenRepositoryHasRegularCommitsNumbering(3)

                runTask()

                val output = ByteArrayOutputStream()
                remoteProject.exec {
                    it.standardOutput = output
                    it.errorOutput = output
                    it.commandLine("git")
                    it.args("tag")
                }
                assertThat(output.toString()).isEqualToIgnoringNewLines("x1.1.1.3")
            }
        }
    }

    private fun givenWeHaveLocalAndRemoteRepositories() {
        localProjectDir = createTempDir()
        remoteProjectDir = createTempDir()

        localProject = ProjectBuilder.builder().withProjectDir(localProjectDir).build()
        remoteProject = ProjectBuilder.builder().withProjectDir(remoteProjectDir).build()

        createGitRepo(localProject)
        createGitRepo(remoteProject)

        addRemoteToLocal()
    }

    private fun addRemoteToLocal() {
        localProject.exec {
            it.commandLine("git")
            it.args("remote", "add", "origin", remoteProjectDir.path)
        }
    }

    private fun createGitRepo(project: Project) {
        project.exec {
            it.commandLine("git")
            it.args("init")
        }
    }

    private fun givenProjectIsUsingDefaultConfiguration() {
        buildFile = File("$localProjectDir/build.gradle").withContents(
            this::class.java.getResourceAsStream("/default-build.gradle").readBytes()
        )
        localProject = ProjectBuilder.builder().withProjectDir(localProjectDir).build()
    }

    private fun givenProjectIsUsingCustomConfiguration() {
        buildFile = File("$localProjectDir/build.gradle").withContents(
            this::class.java.getResourceAsStream("/configured-build.gradle").readBytes()
        )
        localProject = ProjectBuilder.builder().withProjectDir(localProjectDir).build()
    }

    private fun givenRepositoryHasRegularCommitsNumbering(number: Int) {
        createCommits("Hello", number)
    }

    private fun createCommits(message: String, number: Int) {
        for (i in 1..number) {
            localProject.exec {
                it.commandLine("git")
                it.args("commit", "-m", "Commit$i\n\n$message", "--allow-empty")
            }
        }
    }

    private fun runTask(): BuildResult {
        return GradleRunner.create()
            .withProjectDir(localProjectDir)
            .withPluginClasspath()
            .withArguments("-q", "tagVersion")
            .forwardStdOutput(PrintWriter(System.out))
            .build()
    }
}
