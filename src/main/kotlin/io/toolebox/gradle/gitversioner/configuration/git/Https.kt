package io.toolebox.gradle.gitversioner.configuration.git

import org.gradle.api.Action

class Https(action: Action<Https>) {

    var username: String? = null
    var password: String? = null
    var token: String? = null

    init {
        action.execute(this)
    }
}
