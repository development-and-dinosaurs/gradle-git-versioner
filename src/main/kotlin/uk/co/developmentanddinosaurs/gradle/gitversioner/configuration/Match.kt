package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import org.gradle.api.Action

/**
 * Configuration to specify the patterns to match against for version increments.
 */
class Match(action: Action<Match>) {
    /**
     * The pattern to match for major version increments.
     *
     * Default is "[major]".
     */
    var major = "[major]"

    /**
     * The pattern to match for minor version increments.
     *
     * Default is "[minor]".
     */
    var minor = "[minor]"

    /**
     * The pattern to match for patch version increments.
     *
     * Default is "[patch]".
     */
    var patch = "[patch]"

    init {
        action.execute(this)
    }
}
