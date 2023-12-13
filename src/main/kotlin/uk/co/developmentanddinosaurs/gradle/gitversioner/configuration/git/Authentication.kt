package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.git

import groovy.lang.Closure
import org.gradle.api.Action

/**
 * Configuration to specify the authentication behaviour.
 */
class Authentication(action: Action<Authentication>) {
    /**
     * The SSH configuration to use for authentication.
     */
    var ssh = Ssh(Action { })

    /**
     * The HTTPS configuration to use for authentication.
     */
    var https =
        Https(Action { })

    init {
        action.execute(this)
    }

    fun ssh(action: Action<Ssh>) {
        ssh = Ssh(action)
    }

    fun ssh(closure: Closure<String>) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = ssh
        closure.call()
    }

    fun https(action: Action<Https>) {
        https = Https(action)
    }

    fun https(closure: Closure<Https>) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = https
        closure.call()
    }
}
