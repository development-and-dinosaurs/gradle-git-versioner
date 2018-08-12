package io.toolebox.gradle.gitversioner.configuration

import org.gradle.api.Action

class Tag(action: Action<Tag>) {

    var prefix = "v"

    init {
        action.execute(this)
    }

}
