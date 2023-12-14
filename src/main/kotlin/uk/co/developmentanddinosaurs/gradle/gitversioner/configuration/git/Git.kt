package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.git

import groovy.lang.Closure
import org.gradle.api.Action

/**
 * Configuration to specify the Git behaviour.
 */
class Git(action: Action<Git>) {
    /**
     * The authentication configuration to use for Git.
     */
    var authentication =
        Authentication(Action { })

    init {
        action.execute(this)
    }

    fun authentication(action: Action<Authentication>) {
        authentication =
            Authentication(
                action,
            )
    }

    fun authentication(closure: Closure<Authentication>) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = authentication
        closure.call()
    }
}
