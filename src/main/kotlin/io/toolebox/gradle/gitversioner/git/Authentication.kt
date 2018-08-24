package io.toolebox.gradle.gitversioner.git

import groovy.lang.Closure
import org.gradle.api.Action

class Authentication(action: Action<Authentication>) {

    var ssh = Ssh(Action { })

    var https = Https(Action { })

    init {
        action.execute(this)
    }

    fun ssh(closure: Closure<Ssh>) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = ssh
        closure.call()
    }

    fun https(closure: Closure<Https>) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = https
        closure.call()
    }
}
