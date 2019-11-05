package io.toolebox.gradle.gitversioner.git

import groovy.lang.Closure
import org.gradle.api.Action

class Authentication(action: Action<Authentication>) {

    var ssh = Ssh(Action { })

    var https = Https(Action { })

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
