package uk.co.developmentanddinosaurs.gradle.gitversioner

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.TaggerExtensionConfig
import uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.VersionerExtension
import uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.VersionerExtensionConfig
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.tag.GitTagger
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.version.Versioner
import uk.co.developmentanddinosaurs.gradle.gitversioner.tasks.PrintVersionTask
import uk.co.developmentanddinosaurs.gradle.gitversioner.tasks.TagVersionTask
import java.io.File

/**
 * A custom Gradle plugin that adds tasks related to semantic versioning via Git.
 */
class VersionerPlugin : Plugin<Project> {
    /**
     * Apply the plugin to the project.
     *
     * @param project the project to apply the plugin to
     */
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
            val projectVersion = project.version.toString()
            printVersionTask.get().version = projectVersion
            tagVersionTask.get().version = projectVersion
        }
    }
}
