package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.git

import org.gradle.api.Action

/**
 * Configuration to specify the HTTPS behaviour.
 */
class Https(action: Action<Https>) {
    /**
     * The username to authenticate with git.
     */
    var username: String? = null

    /**
     * The password to authenticate with git.
     */
    var password: String? = null

    /**
     * The token to authenticate with git.
     */
    var token: String? = null

    init {
        action.execute(this)
    }
}
