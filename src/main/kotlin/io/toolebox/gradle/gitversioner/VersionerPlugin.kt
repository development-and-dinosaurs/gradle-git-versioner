package io.toolebox.gradle.gitversioner

import io.toolebox.gradle.gitversioner.configuration.VersionerPluginExtension
import io.toolebox.gradle.gitversioner.core.tag.GitTagger
import io.toolebox.gradle.gitversioner.core.tag.TaggerConfig
import io.toolebox.gradle.gitversioner.tag.TagVersionTask
import io.toolebox.gradle.gitversioner.version.PrintVersionTask
import io.toolebox.gradle.gitversioner.version.Versioner
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class VersionerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("versioner", VersionerPluginExtension::class.java)
        val versioner = Versioner(project)
        val printVersionTask = project.tasks.create("printVersion", PrintVersionTask::class.java) {
            it.versioner = versioner
        }
        val tagVersionTask = project.tasks.register("tagVersion", TagVersionTask::class.java) {
            it.versioner = versioner
            it.tagger = createTagger(project, extension)
        }
        project.afterEvaluate {
            printVersionTask.startFrom = extension.startFrom
            printVersionTask.match = extension.match
            printVersionTask.pattern = extension.pattern
            val versionTask = tagVersionTask.get()
            versionTask.startFrom = extension.startFrom
            versionTask.match = extension.match
            versionTask.pattern = extension.pattern

            val version = versioner.version(extension.startFrom, extension.match)
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
