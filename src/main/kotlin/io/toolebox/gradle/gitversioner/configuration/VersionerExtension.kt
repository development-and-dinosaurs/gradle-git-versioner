package io.toolebox.gradle.gitversioner.configuration

import io.toolebox.gradle.gitversioner.configuration.git.Git
import io.toolebox.gradle.gitversioner.core.version.Versioner
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project

open class VersionerExtension @Inject constructor(
    private val project: Project,
    private val versioner: Versioner
) {

    var calculatedVersion: String? = null

    var startFrom = StartFrom(Action { })

    var match = Match(Action { })

    var tag = Tag(Action { })

    var git = Git(Action { })

    var pattern = Pattern(Action { })

    fun startFrom(action: Action<StartFrom>) {
        startFrom = StartFrom(action)
    }

    fun match(action: Action<Match>) {
        match = Match(action)
    }

    fun tag(action: Action<Tag>) {
        tag = Tag(action)
    }

    fun git(action: Action<Git>) {
        git = Git(action)
    }

    fun pattern(action: Action<Pattern>) {
        pattern = Pattern(action)
    }

    fun apply() {
        val config = VersionerExtensionConfig(this)
        val version = versioner.version(config)
        calculatedVersion = version.print(config.pattern).also {
            project.version = it
        }
    }
}
