package io.toolebox.gradle.gitversioner.version

import io.toolebox.gradle.gitversioner.configuration.MavenSnapshot
import io.toolebox.gradle.gitversioner.configuration.Match
import io.toolebox.gradle.gitversioner.configuration.StartFrom
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class PrintVersionTask : DefaultTask() {
    @Input
    lateinit var versioner: Versioner
    @Input
    lateinit var startFrom: StartFrom
    @Input
    lateinit var mavenSnapshot: MavenSnapshot
    @Input
    lateinit var match: Match

    @TaskAction
    fun printVersion() {
        println(versioner.version(startFrom, mavenSnapshot, match))
    }
}
