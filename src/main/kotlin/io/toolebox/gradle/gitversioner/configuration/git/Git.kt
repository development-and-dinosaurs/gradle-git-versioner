package io.toolebox.gradle.gitversioner.configuration.git

import groovy.lang.Closure
import org.gradle.api.Action

class Git(action: Action<Git>) {

    var authentication =
        Authentication(Action { })

    init {
        action.execute(this)
    }

    fun authentication(action: Action<Authentication>) {
        authentication =
            Authentication(
                action
            )
    }

    fun authentication(closure: Closure<Authentication>) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = authentication
        closure.call()
    }
}
