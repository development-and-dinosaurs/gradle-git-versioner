package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import org.gradle.api.Action

/**
 * Configuration used for tagging.
 */
class Tag(action: Action<Tag>) {
    /**
     * The prefix to use for the version tag.
     *
     * Default is "v".
     */
    var prefix = "v"

    /**
     * Whether to use the commit message as the tag message.
     *
     * Default is "false".
     */
    var useCommitMessage = false

    init {
        action.execute(this)
    }
}
