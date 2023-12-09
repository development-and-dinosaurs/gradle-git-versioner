package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import org.gradle.api.Action
import org.gradle.api.Project
import uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.git.Git
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.version.Versioner
import javax.inject.Inject

open class VersionerExtension
    @Inject
    constructor(
        private val project: Project,
        private val versioner: Versioner,
    ) {
        var calculatedVersion: String? = null

        var startFrom = StartFrom { }

        var match = Match { }

        var tag = Tag { }

        var git = Git { }

        var pattern = Pattern { }

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
            val versionString = version.print(config.pattern)
            calculatedVersion = versionString
            project.version = versionString
        }
    }
