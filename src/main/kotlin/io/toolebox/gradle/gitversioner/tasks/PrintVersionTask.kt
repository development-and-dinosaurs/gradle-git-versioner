package io.toolebox.gradle.gitversioner.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class PrintVersionTask : DefaultTask() {
    @Input
    lateinit var version: String

    @TaskAction
    fun printVersion() {
        println(version)
    }
}
