package uk.co.developmentanddinosaurs.gradle.gitversioner.tasks

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.transport.URIish
import uk.co.developmentanddinosaurs.gradle.gitversioner.util.Gradle
import uk.co.developmentanddinosaurs.gradle.gitversioner.util.Project
import java.io.File

class TagVersionTaskSpec : StringSpec({

    val directory = File("build/tmp/functionalTest/TagVersionTaskSpec/local")
    val remoteDir = File("build/tmp/functionalTest/TagVersionTaskSpec/remote")
    val project = Project.createProject(directory)
    val gradle = Gradle(directory)
    lateinit var localGit: Git
    lateinit var remoteGit: Git

    fun lastTag(git: Git) = RevWalk(git.repository).parseTag(git.tagList().call()[0].objectId)


    fun createRepository(folder: File): Git {
        return Git.init().setDirectory(folder).call()
    }

    fun addRemote(git: Git) {
        git.remoteAdd().setName("origin").setUri(URIish(remoteDir.absolutePath)).call()
    }

    fun addCommit(git: Git) {
        git.commit().setSign(false).setAllowEmpty(true).setMessage("[major]").call()
    }

    beforeTest {
        localGit = createRepository(directory)
        remoteGit = createRepository(remoteDir)
        addRemote(localGit)
        addCommit(localGit)
    }

    afterTest {
        directory.deleteRecursively()
        remoteDir.deleteRecursively()
    }

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


})
