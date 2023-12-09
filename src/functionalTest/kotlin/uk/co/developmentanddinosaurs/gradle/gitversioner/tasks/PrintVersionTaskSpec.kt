package uk.co.developmentanddinosaurs.gradle.gitversioner.tasks

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import uk.co.developmentanddinosaurs.gradle.gitversioner.util.Gradle
import uk.co.developmentanddinosaurs.gradle.gitversioner.util.Project
import java.io.File

class PrintVersionTaskSpec : StringSpec({

     val directory = File("build/tmp/functionalTest/PrintVersionTaskSpec")
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

    "prints version using default configuration when none is supplied in Groovy" {
        project.withSettingsFile().withGit().withGroovyGradleFile("default")
        addCommits(project)

        val result = gradle.runTask("printVersion")

        result.output shouldContain "1.1.1.1"
    }

    "prints version using provided configuration when supplied in Groovy" {
        project.withSettingsFile().withGit().withGroovyGradleFile("configured")
        addCommits(project)

        val result = gradle.runTask("printVersion")

        result.output shouldContain "2.1.1-4"
    }

    "prints version using default configuration when none is supplied in Kotlin" {
        project.withSettingsFile().withGit().withKotlinGradleFile("default")
        addCommits(project)

        val result = gradle.runTask("printVersion")

        result.output shouldContain "1.1.1.1"
    }

    "prints version using provided configuration when supplied in Kotlin" {
        project.withSettingsFile().withGit().withKotlinGradleFile("configured")
        addCommits(project)

        val result = gradle.runTask("printVersion")

        result.output shouldContain "2.1.1-4"
    }
})
