package io.toolebox.gradle.gitversioner.tasks

import io.toolebox.gradle.gitversioner.core.tag.GitTagger
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class TagVersionTask @Inject constructor(private val tagger: GitTagger) : DefaultTask() {

    @Input
    lateinit var version: String

    @TaskAction
    fun tagVersion() {
        tagger.tag(version)
    }
}
