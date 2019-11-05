package io.toolebox.gradle.gitversioner.tag

import io.kotlintest.Description
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.toolebox.gradle.gitversioner.withContents
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter

class TagVersionTaskSpec : FreeSpec() {

    private lateinit var localProjectDir: File
    private lateinit var remoteProjectDir: File
    private lateinit var buildFile: File
    private lateinit var localProject: Project
    private lateinit var remoteProject: Project

    override fun beforeTest(description: Description) {
        super.beforeTest(description)
        givenWeHaveLocalAndRemoteRepositories()
    }

    init {
        "Creates local tag and pushes to remote repository" {
            givenProjectIsUsingDefaultConfiguration()
            givenRepositoryHasRegularCommitsNumbering(3)

            runTagVersionTask()

            val output = runGitTagOnRemote()
            output.toString() shouldBe "v0.0.0.3\n"
        }
        "Creates tag using configuration when specified" {
            givenProjectIsUsingCustomConfiguration()
            givenRepositoryHasRegularCommitsNumbering(5)

            runTagVersionTask()

            val output = runGitTagOnRemote()
            output.toString() shouldBe "x1.1.1.5\n"
        }
    }

    private fun runGitTagOnRemote(): ByteArrayOutputStream {
        val output = ByteArrayOutputStream()
        remoteProject.exec {
            it.standardOutput = output
            it.errorOutput = output
            it.commandLine("git", "tag")
        }
        return output
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
            it.commandLine("git remote add origin ${remoteProjectDir.path}".split(" "))
        }
    }

    private fun createGitRepo(project: Project) {
        project.exec {
            it.commandLine("git", "init")
        }
    }

    private fun givenProjectIsUsingDefaultConfiguration() {
        buildFile = File("$localProjectDir/build.gradle").withContents(
            this::class.java.getResourceAsStream("/default-build.gradle").readBytes()
        )
        File("$localProjectDir/settings.gradle").createNewFile()
        localProject = ProjectBuilder.builder().withProjectDir(localProjectDir).build()
    }

    private fun givenProjectIsUsingCustomConfiguration() {
        buildFile = File("$localProjectDir/build.gradle").withContents(
            this::class.java.getResourceAsStream("/configured-build.gradle").readBytes()
        )
        File("$localProjectDir/settings.gradle").createNewFile()
        localProject = ProjectBuilder.builder().withProjectDir(localProjectDir).build()
    }

    private fun givenRepositoryHasRegularCommitsNumbering(number: Int) {
        createCommits(number)
    }

    private fun createCommits(number: Int) {
        for (i in 1..number) {
            localProject.exec {
                it.commandLine("git commit -m Commit$i --allow-empty".split(" "))
            }
        }
    }

    private fun runTagVersionTask(): BuildResult {
        return GradleRunner.create()
            .withProjectDir(localProjectDir)
            .withPluginClasspath()
            .withArguments("-q", "tagVersion")
            .forwardStdOutput(PrintWriter(System.out))
            .build()
    }
}
