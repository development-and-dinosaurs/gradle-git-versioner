package io.toolebox.gradle.gitversioner.git

import groovy.lang.Closure
import org.gradle.api.Action

class Https(action: Action<Https>) {

    var username: String? = null
    var password: String? = null
    var token: String? = null

    init {
        action.execute(this)
    }

}
