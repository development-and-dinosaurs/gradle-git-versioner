package io.toolebox.gradle.gitversioner.core

import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.toolebox.gradle.gitversioner.core.version.Versioner
import io.toolebox.gradle.gitversioner.core.version.VersionerConfig
import org.eclipse.jgit.api.Git
import java.io.File

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
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("major", 3)

            val version = versioner.version()

            version.major shouldBe 3
        }
        "Increments minor version for commit messages matching default minor regex" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("minor", 2)

            val version = versioner.version()

            version.minor shouldBe 2
        }
        "Increments patch version for commit messages matching default patch regex" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("patch", 1)

            val version = versioner.version()

            version.patch shouldBe 1
        }
        "Increments commit version for commit messages matching not matching any regex" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("hello", 4)

            val version = versioner.version()

            version.commit shouldBe 4
        }
        "Major version increment resets minor, patch, and commit versions" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("hello", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)
            givenRepositoryHasTypeCommitsNumbering("minor", 1)
            givenRepositoryHasTypeCommitsNumbering("major", 1)

            val version = versioner.version()

            version.major shouldBe 1
            version.minor shouldBe 0
            version.patch shouldBe 0
            version.commit shouldBe 0
        }
        "Minor version increment resets patch and commit versions" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("hello", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)
            givenRepositoryHasTypeCommitsNumbering("minor", 1)

            val version = versioner.version()

            version.minor shouldBe 1
            version.patch shouldBe 0
            version.commit shouldBe 0
        }
        "Patch version increment resets commit versions" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("hello", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)

            val version = versioner.version()

            version.patch shouldBe 1
            version.commit shouldBe 0
        }
        "Commit version increment resets nothing" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("major", 1)
            givenRepositoryHasTypeCommitsNumbering("minor", 1)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)
            givenRepositoryHasTypeCommitsNumbering("hello", 1)

            val version = versioner.version()

            version.major shouldBe 1
            version.minor shouldBe 1
            version.patch shouldBe 1
            version.commit shouldBe 1
        }
        "Works even when there's loads of commits" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("major", 100)
            givenRepositoryHasTypeCommitsNumbering("minor", 100)
            givenRepositoryHasTypeCommitsNumbering("patch", 100)
            givenRepositoryHasTypeCommitsNumbering("hello", 100)

            val version = versioner.version()

            version.major shouldBe 100
            version.minor shouldBe 100
            version.patch shouldBe 100
            version.commit shouldBe 100
        }
        "Increments major version from point specified in configuration" {
            val versioner = createVersioner(startFromMajor = 1)
            givenRepositoryHasTypeCommitsNumbering("major", 1)

            val version = versioner.version()

            version.major shouldBe 2
        }
        "Increments minor version from point specified in configuration" {
            val versioner = createVersioner(startFromMinor = 2)
            givenRepositoryHasTypeCommitsNumbering("minor", 1)

            val version = versioner.version()

            version.minor shouldBe 3
        }
        "Increments patch version from point specified in configuration" {
            val versioner = createVersioner(startFromPatch = 3)
            givenRepositoryHasTypeCommitsNumbering("patch", 1)

            val version = versioner.version()

            version.patch shouldBe 4
        }
        "Increments major version based on major match regex specified in configuration" {
            val versioner = createVersioner(matchMajor = "trex")
            givenRepositoryHasTypeCommitsNumbering("trex", 1)

            val version = versioner.version()

            version.major shouldBe 1
        }
        "Increments minor version based on minor match regex specified in configuration" {
            val versioner = createVersioner(matchMinor = "stego")
            givenRepositoryHasTypeCommitsNumbering("stego", 1)

            val version = versioner.version()

            version.minor shouldBe 1
        }
        "Increments patch version based on patch match regex specified in configuration" {
            val versioner = createVersioner(matchPatch = "compy")
            givenRepositoryHasTypeCommitsNumbering("compy", 1)

            val version = versioner.version()

            version.patch shouldBe 1
        }
        "Version includes current branch" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("hello", 1)

            val version = versioner.version()

            version.branch shouldBe "master"
        }
        "Version includes commit hash from HEAD" {
            val versioner = createVersioner()
            givenRepositoryHasTypeCommitsNumbering("hello", 1)

            val version = versioner.version()

            version.hash shouldBe git.repository.findRef("HEAD").objectId.name
        }
    }

    private fun createVersioner(
        startFromMajor: Int = 0,
        startFromMinor: Int = 0,
        startFromPatch: Int = 0,
        matchMajor: String = "\\[major]",
        matchMinor: String = "\\[minor]",
        matchPatch: String = "\\[patch]"
    ) =
        Versioner(
            projectDir, VersionerConfig(
                startFromMajor = startFromMajor,
                startFromMinor = startFromMinor,
                startFromPatch = startFromPatch,
                matchMajor = Regex(matchMajor),
                matchMinor = Regex(matchMinor),
                matchPatch = Regex(matchPatch)
            )
        )

    private fun givenRepositoryHasTypeCommitsNumbering(message: String, number: Int) {
        createCommits("[$message]", number)
    }

    private fun createCommits(message: String, number: Int) {
        for (i in 1..number) {
            git.commit().setAllowEmpty(true).setMessage(message).call()
        }
    }
}
