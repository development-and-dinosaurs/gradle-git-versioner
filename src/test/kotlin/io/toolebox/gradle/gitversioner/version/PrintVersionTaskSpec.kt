package io.toolebox.gradle.gitversioner.version

import io.kotlintest.Description
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.toolebox.gradle.gitversioner.withContents
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Ignore
import java.io.File
import java.io.PrintWriter

class PrintVersionTaskSpec : FreeSpec() {

//    private lateinit var projectDir: File
//    private lateinit var buildFile: File
//    private lateinit var project: Project
//
//    override fun beforeTest(description: Description) {
//        super.beforeTest(description)
//        givenProjectExists()
//        givenProjectContainsGitRepository()
//    }
//
//    init {
//        "Increments major version for commit messages matching major regex" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasMajorCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "3.0.0\n"
//        }
//        "Increments minor version for commit messages matching minor regex" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasMinorCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "0.3.0\n"
//        }
//        "Increments minor version for commit messages matching patch regex" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasPatchCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "0.0.3\n"
//        }
//        "Increments build version for commit messages not matching any regex" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasRegularCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "0.0.0.3\n"
//        }
//        "Major version increment resets minor, patch, and build versions" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasMinorCommitsNumbering(3)
//            givenRepositoryHasPatchCommitsNumbering(3)
//            givenRepositoryHasRegularCommitsNumbering(3)
//            givenRepositoryHasMajorCommitsNumbering(1)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "1.0.0\n"
//        }
//        "Minor version increment resets patch and build versions" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasPatchCommitsNumbering(3)
//            givenRepositoryHasRegularCommitsNumbering(3)
//            givenRepositoryHasMinorCommitsNumbering(1)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "0.1.0\n"
//        }
//        "Patch version increment resets build version" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasRegularCommitsNumbering(3)
//            givenRepositoryHasPatchCommitsNumbering(1)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "0.0.1\n"
//        }
//        "Build version increment resets nothing" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasMajorCommitsNumbering(1)
//            givenRepositoryHasMinorCommitsNumbering(1)
//            givenRepositoryHasPatchCommitsNumbering(1)
//            givenRepositoryHasRegularCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "1.1.1.3\n"
//        }
//        "Works even when there's loads of commits" {
//            givenProjectIsUsingDefaultConfiguration()
//            givenRepositoryHasRegularCommitsNumbering(100)
//            givenRepositoryHasPatchCommitsNumbering(3)
//            givenRepositoryHasMinorCommitsNumbering(1)
//            givenRepositoryHasPatchCommitsNumbering(10)
//            givenRepositoryHasMajorCommitsNumbering(1)
//            givenRepositoryHasPatchCommitsNumbering(100)
//            givenRepositoryHasMinorCommitsNumbering(13)
//            givenRepositoryHasPatchCommitsNumbering(10)
//            givenRepositoryHasRegularCommitsNumbering(10)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "1.13.10.10\n"
//        }
//        "Increments major version from starting point specified in configuration" {
//            givenProjectIsUsingCustomConfiguration()
//            givenRepositoryHasMajorCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "4.0.0\n"
//        }
//        "Increments minor version from starting point specified in configuration" {
//            givenProjectIsUsingCustomConfiguration()
//            givenRepositoryHasMinorCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "1.4.0\n"
//        }
//        "Increments patch version from starting point specified in configuration" {
//            givenProjectIsUsingCustomConfiguration()
//            givenRepositoryHasPatchCommitsNumbering(3)
//
//            val result = runPrintVersionTask()
//
//            result.output shouldBe "1.1.4\n"
//        }
//    }
//
//    private fun givenProjectIsUsingDefaultConfiguration() {
//        buildFile = File("$projectDir/build.gradle").withContents(
//            this::class.java.getResourceAsStream("/default-build.gradle")
//        )
//    }
//
//    private fun givenProjectIsUsingCustomConfiguration() {
//        buildFile = File("$projectDir/build.gradle").withContents(
//            this::class.java.getResourceAsStream("/configured-build.gradle")
//        )
//    }
//
//    private fun givenProjectExists() {
//        projectDir = createTempDir()
//        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
//    }
//
//    private fun givenProjectContainsGitRepository() {
//        project.exec {
//            it.commandLine("git", "init")
//        }
//    }
//
//    private fun givenRepositoryHasMajorCommitsNumbering(number: Int) {
//        createCommits("[major]", number)
//    }
//
//    private fun givenRepositoryHasMinorCommitsNumbering(number: Int) {
//        createCommits("[minor]", number)
//    }
//
//    private fun givenRepositoryHasPatchCommitsNumbering(number: Int) {
//        createCommits("[patch]", number)
//    }
//
//    private fun givenRepositoryHasRegularCommitsNumbering(number: Int) {
//        createCommits("Hello", number)
//    }
//
//    private fun createCommits(message: String, number: Int) {
//        for (i in 1..number) {
//            project.exec {
//                it.commandLine("git commit -m Commit$i\n\n$message --allow-empty".split(" "))
//            }
//        }
//    }
//
//    private fun runPrintVersionTask(): BuildResult {
//        return GradleRunner.create()
//            .withProjectDir(projectDir)
//            .withPluginClasspath()
//            .withArguments("-q", "printVersion")
//            .forwardStdOutput(PrintWriter(System.out))
//            .build()
//    }
}
