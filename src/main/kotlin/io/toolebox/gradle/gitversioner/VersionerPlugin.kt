package io.toolebox.gradle.gitversioner

import io.toolebox.gradle.gitversioner.configuration.VersionerPluginExtension
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
        project.afterEvaluate {
            printVersionTask.startFrom = extension.startFrom
            printVersionTask.match = extension.match
            val version = versioner.version(extension.startFrom, extension.match)
            project.version = version
        }
    }

}
