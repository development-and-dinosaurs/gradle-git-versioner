package io.toolebox.gradle.gitversioner.tasks

import io.toolebox.gradle.gitversioner.core.version.Versioner
import io.toolebox.gradle.gitversioner.core.version.VersionerConfig
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class PrintVersionTask @Inject constructor(
    private val versioner: Versioner,
    private val config: VersionerConfig
) : DefaultTask() {

    @TaskAction
    fun printVersion() {
        println(versioner.version(config).print(config.pattern))
    }
}
