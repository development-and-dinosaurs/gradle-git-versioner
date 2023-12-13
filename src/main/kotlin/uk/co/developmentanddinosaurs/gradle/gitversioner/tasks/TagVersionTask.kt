package uk.co.developmentanddinosaurs.gradle.gitversioner.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.tag.GitTagger
import javax.inject.Inject

/**
 * Task for performing version tagging via `tagVersion` command.
 */
abstract class TagVersionTask
    @Inject
    constructor(private val tagger: GitTagger) : DefaultTask() {
        init {
            group = "versioning"
            description = "Tag the calculated version"
        }

        /**
         * The version to tag when running the action.
         */
        @get:Input
        lateinit var version: String

        /**
         * Action to run when the `tagVersion` task is executed.
         */
        @TaskAction
        fun tagVersion() {
            tagger.tag(version)
        }
    }
