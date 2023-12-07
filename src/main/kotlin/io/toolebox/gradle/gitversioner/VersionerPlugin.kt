package io.toolebox.gradle.gitversioner

import io.toolebox.gradle.gitversioner.configuration.TaggerExtensionConfig
import io.toolebox.gradle.gitversioner.configuration.VersionerExtension
import io.toolebox.gradle.gitversioner.configuration.VersionerExtensionConfig
import io.toolebox.gradle.gitversioner.core.tag.GitTagger
import io.toolebox.gradle.gitversioner.core.version.Versioner
import io.toolebox.gradle.gitversioner.tasks.PrintVersionTask
import io.toolebox.gradle.gitversioner.tasks.TagVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import java.io.File

class VersionerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val gitFolder = File("${project.rootDir}/.git")
        val versioner = Versioner(gitFolder)
        val extension = project.extensions.create<VersionerExtension>("versioner", project, versioner)
        val gitTagger = GitTagger(gitFolder, TaggerExtensionConfig(extension))
        val printVersionTask = project.tasks.register<PrintVersionTask>("printVersion")
        val tagVersionTask = project.tasks.register<TagVersionTask>("tagVersion", gitTagger)
        project.afterEvaluate {
            if (extension.calculatedVersion == null) {
                val config = VersionerExtensionConfig(extension)
                val version = versioner.version(config).print(config.pattern)
                project.version = version
            }
            printVersionTask.get().version = project.version.toString()
            tagVersionTask.get().version = project.version.toString()
        }
    }
}
