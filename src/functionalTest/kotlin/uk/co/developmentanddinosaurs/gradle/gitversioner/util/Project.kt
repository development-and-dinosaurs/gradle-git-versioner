package uk.co.developmentanddinosaurs.gradle.gitversioner.util

import org.eclipse.jgit.api.Git
import java.io.File

class Project(private val directory: File) {
    private lateinit var git: Git

    companion object {
        fun createProject(directory: File) = Project(directory)
    }

    fun withGit(): Project {
        git = Git.init().setDirectory(directory).call()
        return this
    }

    fun withCommit(message: String): Project {
        git.commit().setSign(false).setAllowEmpty(true).setMessage(message).call()
        return this
    }

    fun withGroovyGradleFile(name: String): Project {
        return withGradleFile("$name-build.gradle", "build.gradle")
    }

    fun withKotlinGradleFile(name: String): Project {
        return withGradleFile("$name-build.gradle.kts", "build.gradle.kts")
    }

    fun withSettingsFile(): Project {
        return withGradleFile("settings.gradle", "settings.gradle")
    }

    private fun withGradleFile(
        name: String,
        destination: String,
    ): Project {
        File("src/functionalTest/resources/$name").copyTo(File(directory, destination))
        return this
    }
}
