package io.toolebox.gradle.gitversioner

import io.toolebox.gradle.gitversioner.configuration.VersionerExtensionConfig
import io.toolebox.gradle.gitversioner.configuration.VersionerPluginExtension
import io.toolebox.gradle.gitversioner.core.tag.GitTagger
import io.toolebox.gradle.gitversioner.core.tag.TaggerConfig
import io.toolebox.gradle.gitversioner.core.version.Versioner
import io.toolebox.gradle.gitversioner.tasks.PrintVersionTask
import io.toolebox.gradle.gitversioner.tasks.TagVersionTask
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("versioner", VersionerPluginExtension::class.java)
        val versioner = Versioner(File("${project.rootDir}/.git"))
        project.tasks.register("printVersion", PrintVersionTask::class.java, versioner, VersionerExtensionConfig(extension))
        val tagVersionTask = project.tasks.register("tagVersion", TagVersionTask::class.java) {
        }
        project.afterEvaluate {
            val versionTask = tagVersionTask.get()
            versionTask.startFrom = extension.startFrom
            versionTask.match = extension.match
            versionTask.pattern = extension.pattern

            val version = versioner.version(VersionerExtensionConfig(extension))
            project.version = version.print(extension.pattern.pattern)
        }
    }

    private fun createTagger(project: Project, extension: VersionerPluginExtension) =
        GitTagger(
            File("${project.rootDir}/.git"),
            TaggerConfig(
                username = extension.git.authentication.https.username,
                password = extension.git.authentication.https.password,
                token = extension.git.authentication.https.token,
                strictHostChecking = extension.git.authentication.ssh.strictSsl,
                prefix = extension.tag.prefix
            )
        )
}
