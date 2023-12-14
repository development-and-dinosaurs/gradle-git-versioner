package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.git

import org.gradle.api.Action

/**
 * Configuration to specify the SSH behaviour.
 */
class Ssh(action: Action<Ssh>) {
    /**
     * Whether to verify the host public key.
     *
     * Default is "true"
     */
    var strictSsl = true

    init {
        action.execute(this)
    }
}
