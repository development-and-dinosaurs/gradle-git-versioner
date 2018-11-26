package io.toolebox.gradle.gitversioner.configuration

import org.gradle.api.Action

class MavenSnapshot(action: Action<MavenSnapshot>) {

    var enabled = false

    init {
        action.execute(this)
    }
}
