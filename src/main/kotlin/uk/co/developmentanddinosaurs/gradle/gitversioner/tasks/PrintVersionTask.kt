package uk.co.developmentanddinosaurs.gradle.gitversioner.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Task for performing version printing via `printVersion` command.
 */
abstract class PrintVersionTask : DefaultTask() {
    init {
        group = "versioning"
        description = "Print the calculated version"
    }

    /**
     * The version to tag when running the action.
     */
    @get:Input
    lateinit var version: String

    /**
     * Action to run when the `printVersion` task is executed.
     */
    @TaskAction
    fun printVersion() {
        println(version)
    }
}
