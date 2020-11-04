package io.toolebox.gradle.gitversioner.core

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.toolebox.gradle.gitversioner.core.tag.GitTagger
import io.toolebox.gradle.gitversioner.core.tag.TaggerConfig
import java.io.File
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.transport.URIish

class GitTaggerSpec : StringSpec() {

    private val projectDir = File("build/tmp/integrationTest/local")
    private val remoteDir = File("build/tmp/integrationTest/remote")

    private lateinit var localGit: Git
    private lateinit var remoteGit: Git

    override fun beforeTest(testCase: TestCase) {
        localGit = createRepository(projectDir)
        remoteGit = createRepository(remoteDir)
        addCommitToLocalRepository(localGit)
        addRemoteAsLocalOrigin(localGit)
        super.beforeTest(testCase)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        projectDir.deleteRecursively()
        remoteDir.deleteRecursively()
        super.afterTest(testCase, result)
    }

    private fun addCommitToLocalRepository(git: Git) {
        git.commit().setSign(false).setAllowEmpty(true).setMessage("Commit\nCommit").call()
    }

    private fun addRemoteAsLocalOrigin(git: Git) {
        git.remoteAdd().setName("origin").setUri(URIish(remoteDir.absolutePath)).call()
    }

    private fun createRepository(folder: File): Git {
        Git.init().setDirectory(folder).call()
        return Git.open(File(folder.absolutePath + "/.git"))
    }

    init {
        "Creates tag locally and pushes to remote repository" {
            val tagger = createTagger()

            tagger.tag("1.0.0")

            lastTag(localGit).tagName shouldBe "v1.0.0"
            lastTag(remoteGit).tagName shouldBe "v1.0.0"
        }
        "Creates overridden tag locally and pushes to remote repository" {
            val tagger = createTagger(prefix = "x")

            tagger.tag("1.0.0")

            lastTag(localGit).tagName shouldBe "x1.0.0"
        }
        "Creates tag with no message when not specified" {
            val tagger = createTagger()

            tagger.tag("1.0.0")

            lastTag(localGit).fullMessage shouldBe ""
        }
        "Creates tag with message as last commit message" {
            val tagger = createTagger(useCommitMessage = true)

            tagger.tag("1.0.0")

            lastTag(localGit).fullMessage shouldBe "Commit\nCommit"
        }
    }

    private fun createTagger(prefix: String = "v", useCommitMessage: Boolean = false) =
        GitTagger(projectDir, object : TaggerConfig {
            override val username = null
            override val password = null
            override val token = null
            override val strictHostChecking = false
            override val prefix = prefix
            override val useCommitMessage = useCommitMessage
        })

    private fun lastTag(git: Git) =
        RevWalk(git.repository).parseTag(git.tagList().call()[0].objectId)
}
