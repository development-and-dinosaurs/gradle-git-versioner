package io.toolebox.gradle.gitversioner.util

import java.io.File
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

class Gradle(private val directory: File) {

    fun runTask(name: String): BuildResult {
        return GradleRunner.create()
            .withProjectDir(directory)
            .withArguments(name, "-q")
            .withPluginClasspath()
            .forwardOutput()
            .build()
    }
}
