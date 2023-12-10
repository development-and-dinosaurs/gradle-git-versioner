package uk.co.developmentanddinosaurs.gradle.gitversioner.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.tag.GitTagger
import javax.inject.Inject

open class TagVersionTask
    @Inject
    constructor(private val tagger: GitTagger) : DefaultTask() {
        @Input
        lateinit var version: String

        @TaskAction
        fun tagVersion() {
            tagger.tag(version)
        }
    }
