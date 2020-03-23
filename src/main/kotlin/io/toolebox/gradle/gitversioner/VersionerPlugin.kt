package io.toolebox.gradle.gitversioner

import io.toolebox.gradle.gitversioner.configuration.TaggerExtensionConfig
import io.toolebox.gradle.gitversioner.configuration.VersionerExtensionConfig
import io.toolebox.gradle.gitversioner.configuration.VersionerPluginExtension
import io.toolebox.gradle.gitversioner.core.tag.GitTagger
import io.toolebox.gradle.gitversioner.core.version.Versioner
import io.toolebox.gradle.gitversioner.tasks.PrintVersionTask
import io.toolebox.gradle.gitversioner.tasks.TagVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class VersionerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("versioner", VersionerPluginExtension::class.java)
        val gitFolder = File("${project.rootDir}/.git")
        val versioner = Versioner(gitFolder)
        val gitTagger = GitTagger(gitFolder, TaggerExtensionConfig(extension))
        val printVersionTask = project.tasks.register("printVersion", PrintVersionTask::class.java)
        val tagVersionTask =
            project.tasks.register("tagVersion", TagVersionTask::class.java, gitTagger)
        project.afterEvaluate {
            val config = VersionerExtensionConfig(extension)
            val version = versioner.version(config).print(config.pattern)
            project.version = version
            printVersionTask.get().version = version
            tagVersionTask.get().version = version
        }
    }
}
