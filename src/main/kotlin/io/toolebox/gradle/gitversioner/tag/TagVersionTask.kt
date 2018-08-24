package io.toolebox.gradle.gitversioner.tag

import io.toolebox.gradle.gitversioner.configuration.Match
import io.toolebox.gradle.gitversioner.configuration.StartFrom
import io.toolebox.gradle.gitversioner.configuration.Tag
import io.toolebox.gradle.gitversioner.git.Git
import io.toolebox.gradle.gitversioner.version.Versioner
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class TagVersionTask : DefaultTask() {
    @Input
    lateinit var versioner: Versioner
    @Input
    lateinit var tagger: GitTagger
    @Input
    lateinit var startFrom: StartFrom
    @Input
    lateinit var match: Match
    @Input
    lateinit var tag: Tag
    @Input
    lateinit var git: Git

    @TaskAction
    fun tagVersion() {
        val version = versioner.version(startFrom, match)
        tagger.tag(version, tag, git)
    }
}
