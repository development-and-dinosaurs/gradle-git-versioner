package io.toolebox.gradle.gitversioner

import io.toolebox.gradle.gitversioner.configuration.VersionerPluginExtension
import io.toolebox.gradle.gitversioner.tag.GitTagger
import io.toolebox.gradle.gitversioner.tag.TagVersionTask
import io.toolebox.gradle.gitversioner.version.PrintVersionTask
import io.toolebox.gradle.gitversioner.version.Versioner
import org.gradle.api.Plugin
import org.gradle.api.Project


class VersionerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("versioner", VersionerPluginExtension::class.java)
        val versioner = Versioner(project)
        val printVersionTask = project.tasks.create("printVersion", PrintVersionTask::class.java) {
            it.versioner = versioner
        }
        val tagVersionTask = project.tasks.create("tagVersion", TagVersionTask::class.java) {
            it.versioner = versioner
            it.tagger = GitTagger(project)
        }
        project.afterEvaluate {
            printVersionTask.startFrom = extension.startFrom
            printVersionTask.match = extension.match
            printVersionTask.pattern = extension.pattern
            tagVersionTask.startFrom = extension.startFrom
            tagVersionTask.match = extension.match
            tagVersionTask.tag = extension.tag
            tagVersionTask.git = extension.git
            tagVersionTask.pattern = extension.pattern

            val version = versioner.version(extension.startFrom, extension.match)
            project.version = version.print(extension.pattern.pattern)
        }
    }

}
