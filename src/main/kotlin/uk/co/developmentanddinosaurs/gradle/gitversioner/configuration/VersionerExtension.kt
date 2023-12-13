package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import org.gradle.api.Action
import org.gradle.api.Project
import uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.git.Git
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.version.Versioner
import javax.inject.Inject

/**
 * Collects configuration details in the Gradle context.
 *
 * This extension can be used in a gradle build file to provide required configuration, for example:
 *
 * ```
 * versioner {
 *     startFrom {
 *         major = 1
 *         minor = 1
 *         patch = 1
 *     }
 *     match {
 *         major = "trex"
 *         minor = "stego"
 *         patch = "compy"
 *     }
 *     tag {
 *         prefix = "x"
 *         useCommitMessage = true
 *     }
 *     git {
 *         authentication {
 *             ssh {
 *                 strictSsl = false
 *             }
 *             https {
 *                 username = "username"
 *                 password = "password"
 *                 token = "token"
 *             }
 *         }
 *     }
 *     pattern {
 *         pattern = "%M.%m.%p(-%c)"
 *     }
 * }
 * ```
 */
open class VersionerExtension
    @Inject
    constructor(
        private val project: Project,
        private val versioner: Versioner,
    ) {
        /**
         * Holding field for calculated version.
         *
         * This field is set after the version is calculated, and can be interrogated to see whether we need to
         * perform version calculation later.
         */
        var calculatedVersion: String? = null

        /**
         * Configures the versions to start versioning from, for when a project adopts the plugin after having versioned
         * a different way previously. For example:
         *
         * ```
         * startFrom {
         *     major = 1
         *     minor = 1
         *     patch = 1
         * }
         *  ```
         */
        var startFrom = StartFrom { }

        /**
         * Configures the strings to match against when calculated versions. For example:
         *
         * ```
         * match {
         *     major = "trex"
         *     minor = "stego"
         *     patch = "compy"
         * }
         * ```
         */
        var match = Match { }

        /**
         * Configures the tagging behaviour. For example:
         *
         * ```
         * tag {
         *     prefix = "x"
         *     useCommitMessage = true
         * }
         * ```
         */
        var tag = Tag { }

        /**
         * Configures the git behaviour. For example:
         *
         * ```
         * git {
         *     authentication {
         *         ssh {
         *             strictSsl = false
         *         }
         *         https {
         *             username = "username"
         *             password = "password"
         *             token = "token"
         *         }
         *     }
         * }
         */
        var git = Git { }

        /**
         * Configures the version pattern. For example:
         *
         * ```
         * pattern {
         *     pattern = "%M.%m.%p(-%c)"
         * }
         * ```
         */
        var pattern = Pattern { }

        /**
         * Sets the [StartFrom] configuration.
         */
        fun startFrom(action: Action<StartFrom>) {
            startFrom = StartFrom(action)
        }

        /**
         * Sets the [Match] configuration.
         */
        fun match(action: Action<Match>) {
            match = Match(action)
        }

        /**
         * Sets the [Tag] configuration.
         */
        fun tag(action: Action<Tag>) {
            tag = Tag(action)
        }

        /**
         * Sets the [Git] configuration.
         */
        fun git(action: Action<Git>) {
            git = Git(action)
        }

        /**
         * Sets the [Pattern] configuration.
         */
        fun pattern(action: Action<Pattern>) {
            pattern = Pattern(action)
        }

        /**
         * Applies to versioning behaviour ahead of the usual time.
         *
         * This can be used to set the version ahead of time, for example:
         *
         * ```
         * versioner.apply()
         * ```
         *
         * This can be used where the version needs to be used during the configuration phase, before it would have been
         * calculated in the normal lifecycle. Consumers should not rely on this functionality and should prefer to defer
         * their versioning reliance until the end of the configuration phase, or the start of the execution phase. This
         * method can be used where that is not otherwise possible.
         */
        fun apply() {
            val config = VersionerExtensionConfig(this)
            val version = versioner.version(config)
            val versionString = version.print(config.pattern)
            calculatedVersion = versionString
            project.version = versionString
        }
    }
