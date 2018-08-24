package io.toolebox.gradle.gitversioner.git

import org.gradle.api.Action

class Ssh(action: Action<Ssh>) {

    var strictSsl = true

    init {
        action.execute(this)
    }
}
