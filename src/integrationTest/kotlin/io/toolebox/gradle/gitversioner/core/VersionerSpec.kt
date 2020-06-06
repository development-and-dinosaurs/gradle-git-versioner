package io.toolebox.gradle.gitversioner.core

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.toolebox.gradle.gitversioner.core.version.Versioner
import io.toolebox.gradle.gitversioner.core.version.VersionerConfig
import java.io.File
import org.eclipse.jgit.api.Git

class VersionerSpec : StringSpec() {

    private val projectDir = File("build/tmp/integrationTest/local")

    private lateinit var git: Git

    override fun beforeTest(testCase: TestCase) {
        git = createRepository(projectDir)
        super.beforeTest(testCase)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        projectDir.deleteRecursively()
        super.afterTest(testCase, result)
    }

    private fun createRepository(folder: File): Git {
        folder.mkdirs()
        Git.init().setDirectory(folder).call()
        return Git.open(File(folder.absolutePath + "/.git"))
    }

    init {
        "Increments major version for commit messages matching default major regex" {
            givenRepositoryHasTypeCommitsNumbering("major", 3)

            val version = calculateVersion()

            version.major shouldBe 3
        }
        "Increments minor version for commit messages matching default minor regex" {
            givenRepositoryHasTypeCommitsNumbering("minor", 2)

            val version = calculateVersion()

            version.minor shouldBe 2
        }
        "Increments patch version for commit messages matching default patch regex" {
            givenRepositoryHasTypeCommitsNumbering("patch", 1)

            val version = calculateVersion()

            version.patch shouldBe 1
        }
        "Increments commit version for commit messages matching not matching any regex" {
            givenRepositoryHasTypeCommitsNumbering("hello", 4)

            val version = calculateVersion()

            version.commit shouldBe 4
        }
        "Major version increment resets minor, patch, and commit versions" {
            givenRepositoryHasTypeCommitsNumbering("hello", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)
            givenRepositoryHasTypeCommitsNumbering("minor", 1)
            givenRepositoryHasTypeCommitsNumbering("major", 1)

            val version = calculateVersion()

            version.major shouldBe 1
            version.minor shouldBe 0
            version.patch shouldBe 0
            version.commit shouldBe 0
        }
        "Minor version increment resets patch and commit versions" {
            givenRepositoryHasTypeCommitsNumbering("hello", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)
            givenRepositoryHasTypeCommitsNumbering("minor", 1)

            val version = calculateVersion()

            version.minor shouldBe 1
            version.patch shouldBe 0
            version.commit shouldBe 0
        }
        "Patch version increment resets commit versions" {
            givenRepositoryHasTypeCommitsNumbering("hello", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)

            val version = calculateVersion()

            version.patch shouldBe 1
            version.commit shouldBe 0
        }
        "Commit version increment resets nothing" {
            givenRepositoryHasTypeCommitsNumbering("major", 1)
            givenRepositoryHasTypeCommitsNumbering("minor", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)
            givenRepositoryHasTypeCommitsNumbering("hello", 1)

            val version = calculateVersion()

            version.major shouldBe 1
            version.minor shouldBe 1
            version.patch shouldBe 1
            version.commit shouldBe 1
        }
        "Works even when there's loads of commits" {
            givenRepositoryHasTypeCommitsNumbering("major", 100)
            givenRepositoryHasTypeCommitsNumbering("minor", 100)
            givenRepositoryHasTypeCommitsNumbering("patch", 100)
            givenRepositoryHasTypeCommitsNumbering("hello", 100)

            val version = calculateVersion()

            version.major shouldBe 100
            version.minor shouldBe 100
            version.patch shouldBe 100
            version.commit shouldBe 100
        }
        "Increments major version from point specified in configuration" {
            givenRepositoryHasTypeCommitsNumbering("major", 1)

            val version = calculateVersion(startFromMajor = 1)

            version.major shouldBe 2
        }
        "Increments minor version from point specified in configuration" {
            givenRepositoryHasTypeCommitsNumbering("minor", 1)

            val version = calculateVersion(startFromMinor = 2)

            version.minor shouldBe 3
        }
        "Increments patch version from point specified in configuration" {
            givenRepositoryHasTypeCommitsNumbering("patch", 1)

            val version = calculateVersion(startFromPatch = 3)

            version.patch shouldBe 4
        }
        "Increments major version based on major match regex specified in configuration" {
            givenRepositoryHasTypeCommitsNumbering("trex", 1)

            val version = calculateVersion(matchMajor = "trex")

            version.major shouldBe 1
        }
        "Increments minor version based on minor match regex specified in configuration" {
            givenRepositoryHasTypeCommitsNumbering("stego", 1)

            val version = calculateVersion(matchMinor = "stego")

            version.minor shouldBe 1
        }
        "Increments patch version based on patch match regex specified in configuration" {
            givenRepositoryHasTypeCommitsNumbering("compy", 1)

            val version = calculateVersion(matchPatch = "compy")

            version.patch shouldBe 1
        }
        "Version includes current branch" {
            givenRepositoryHasTypeCommitsNumbering("hello", 1)

            val version = calculateVersion()

            version.branch shouldBe "master"
        }
        "Version includes commit hash from HEAD" {
            givenRepositoryHasTypeCommitsNumbering("hello", 1)

            val version = calculateVersion()

            version.hash shouldBe git.repository.findRef("HEAD").objectId.name
        }
    }

    private fun calculateVersion(
        startFromMajor: Int = 0,
        startFromMinor: Int = 0,
        startFromPatch: Int = 0,
        matchMajor: String = "[major]",
        matchMinor: String = "[minor]",
        matchPatch: String = "[patch]"
    ) = Versioner(projectDir).version(object : VersionerConfig {
        override val startFromMajor = startFromMajor
        override val startFromMinor = startFromMinor
        override val startFromPatch = startFromPatch
        override val matchMajor = matchMajor
        override val matchMinor = matchMinor
        override val matchPatch = matchPatch
        override val pattern = ""
    })

    private fun givenRepositoryHasTypeCommitsNumbering(message: String, number: Int) {
        createCommits("[$message]", number)
    }

    private fun createCommits(message: String, number: Int) {
        for (i in 1..number) {
            git.commit().setAllowEmpty(true).setMessage(message).call()
        }
    }
}
