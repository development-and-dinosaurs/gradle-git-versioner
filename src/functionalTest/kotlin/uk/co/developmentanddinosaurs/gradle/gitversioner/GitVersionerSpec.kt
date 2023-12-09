package uk.co.developmentanddinosaurs.gradle.gitversioner

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import uk.co.developmentanddinosaurs.gradle.gitversioner.util.Gradle
import uk.co.developmentanddinosaurs.gradle.gitversioner.util.Project
import java.io.File

class GitVersionerSpec : StringSpec({

    val directory = File("build/tmp/functionalTest/GitVersionerSpec")
    val project = Project.createProject(directory)
    val gradle = Gradle(directory)

    fun addCommits(project: Project) {
        project
            .withCommit("trex")
            .withCommit("stego")
            .withCommit("compy")
            .withCommit("[major]")
            .withCommit("[minor]")
            .withCommit("[patch]")
            .withCommit("message")
    }

    afterTest {
        directory.deleteRecursively()
    }

    "Version is available after forcing version resolution in Groovy" {
        project.withSettingsFile().withGit().withGroovyGradleFile("configured")
        addCommits(project)

        val result = gradle.runTask("printVersionEarly")

        result.output shouldContain "2.1.1-4"
    }
    "Version is available after forcing version resolution in Kotlin" {
        project.withSettingsFile().withGit().withKotlinGradleFile("configured")
        addCommits(project)

        val result = gradle.runTask("printVersionEarly")

        result.output shouldContain "2.1.1-4"
    }
})
