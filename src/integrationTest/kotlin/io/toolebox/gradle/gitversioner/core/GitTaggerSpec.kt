package io.toolebox.gradle.gitversioner.core

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.toolebox.gradle.gitversioner.core.tag.GitTagger
import io.toolebox.gradle.gitversioner.core.tag.TaggerConfig
import org.eclipse.jgit.transport.URIish
import java.io.File
import org.eclipse.jgit.api.Git as JGit

class GitTaggerSpec : StringSpec() {

    private val projectDir = File("build/tmp/integrationTest/local")
    private val remoteDir = File("build/tmp/integrationTest/remote")

    private lateinit var localGit: JGit
    private lateinit var remoteGit: JGit

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

    private fun addCommitToLocalRepository(git: JGit) {
        git.commit().setAllowEmpty(true).setMessage("Commit").call()
    }

    private fun addRemoteAsLocalOrigin(git: JGit) {
        git.remoteAdd().setName("origin").setUri(URIish(remoteDir.absolutePath)).call()
    }

    private fun createRepository(folder: File): JGit {
        JGit.init().setDirectory(folder).call()
        return JGit.open(File(folder.absolutePath + "/.git"))
    }

    init {
        "Creates tag locally and pushes to remote repository" {
            val tagger = createTagger()

            tagger.tag("1.0.0")

            localGit.tagList().call()[0].name shouldBe "refs/tags/v1.0.0"
            remoteGit.tagList().call()[0].name shouldBe "refs/tags/v1.0.0"
        }
        "Creates overridden tag locally and pushes to remote repository" {
            val tagger = createTagger(prefix = "x")

            tagger.tag("1.0.0")

            localGit.tagList().call()[0].name shouldBe "refs/tags/x1.0.0"
            remoteGit.tagList().call()[0].name shouldBe "refs/tags/x1.0.0"
        }
    }

    private fun createTagger(prefix: String = "v") = GitTagger(projectDir, object : TaggerConfig {
        override val username = null
        override val password = null
        override val token = null
        override val strictHostChecking = false
        override val prefix = prefix
    })
}
