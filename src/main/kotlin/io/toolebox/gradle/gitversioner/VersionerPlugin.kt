package io.toolebox.gradle.gitversioner

import org.gradle.api.Plugin
import org.gradle.api.Project


class VersionerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("versioner", VersionerPluginExtension::class.java)
        val versioner = Versioner(project)
        val task = project.tasks.create("printVersion", PrintVersionTask::class.java) {
            it.versioner = versioner
        }
        project.afterEvaluate {
            task.startFrom = extension.startFrom
            task.match = extension.match
            val version = versioner.version(extension.startFrom, extension.match)
            project.version = version
        }
    }

}
