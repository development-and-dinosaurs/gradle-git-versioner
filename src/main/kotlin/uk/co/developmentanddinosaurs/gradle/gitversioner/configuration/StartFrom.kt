package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import org.gradle.api.Action

/**
 * Configuration to specify the version to start from.
 */
class StartFrom(action: Action<StartFrom>) {
    /**
     * The major version to start from.
     *
     * Default is "0".
     */
    var major = 0

    /**
     * The minor version to start from.
     *
     * Default is "0".
     */
    var minor = 0

    /**
     * The patch version to start from.
     *
     * Default is "0".
     */
    var patch = 0

    init {
        action.execute(this)
    }
}
