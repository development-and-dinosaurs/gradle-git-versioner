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
            .withJacoco()
            .forwardOutput()
            .build()
    }

    fun GradleRunner.withJacoco(): GradleRunner {
        File("build/testkit/functionalTest/testkit-gradle.properties")
            .copyTo(File(projectDir, "gradle.properties"))
        return this
    }
}
