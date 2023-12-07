package io.toolebox.gradle.gitversioner.tasks

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.toolebox.gradle.gitversioner.util.Gradle
import io.toolebox.gradle.gitversioner.util.Project
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.transport.URIish
import java.io.File

class TagVersionTaskSpec : StringSpec() {
    private val directory = File("build/tmp/functionalTest/TagVersionTaskSpec/local")
    private val remoteDir = File("build/tmp/functionalTest/TagVersionTaskSpec/remote")
    private var project = Project.createProject(directory)
    private var gradle = Gradle(directory)
    private lateinit var localGit: Git
    private lateinit var remoteGit: Git

    override fun beforeTest(testCase: TestCase) {
        localGit = createRepository(directory)
        remoteGit = createRepository(remoteDir)
        addRemote(localGit)
        addCommit(localGit)
        super.beforeTest(testCase)
    }

    private fun createRepository(folder: File): Git {
        return Git.init().setDirectory(folder).call()
    }

    private fun addRemote(git: Git) {
        git.remoteAdd().setName("origin").setUri(URIish(remoteDir.absolutePath)).call()
    }

    private fun addCommit(git: Git) {
        git.commit().setSign(false).setAllowEmpty(true).setMessage("[major]").call()
    }

    override fun afterTest(
        testCase: TestCase,
        result: TestResult,
    ) {
        directory.deleteRecursively()
        remoteDir.deleteRecursively()
        super.afterTest(testCase, result)
    }

    init {
        "Creates tag locally and pushes to remote repository using default configuration when none is supplied in Groovy" {
            project.withSettingsFile().withGroovyGradleFile("default")

            gradle.runTask("tagVersion")

            lastTag(localGit).tagName shouldBe "v1.0.0"
            lastTag(remoteGit).tagName shouldBe "v1.0.0"
        }

        "Creates tag locally and pushes to remote repository using provided configuration when supplied in Groovy" {
            project.withSettingsFile().withGroovyGradleFile("configured")

            gradle.runTask("tagVersion")

            lastTag(localGit).tagName shouldBe "x1.1.1-1"
            lastTag(remoteGit).tagName shouldBe "x1.1.1-1"
        }

        "Creates tag locally and pushes to remote repository using default configuration when none is supplied in Kotlin" {
            project.withSettingsFile().withKotlinGradleFile("default")

            gradle.runTask("tagVersion")

            lastTag(localGit).tagName shouldBe "v1.0.0"
            lastTag(remoteGit).tagName shouldBe "v1.0.0"
        }

        "Creates tag locally and pushes to remote repository using provided configuration when supplied in Kotlin" {
            project.withSettingsFile().withKotlinGradleFile("configured")

            gradle.runTask("tagVersion")

            lastTag(localGit).tagName shouldBe "x1.1.1-1"
            lastTag(remoteGit).tagName shouldBe "x1.1.1-1"
        }
    }

    private fun lastTag(git: Git) = RevWalk(git.repository).parseTag(git.tagList().call()[0].objectId)
}
