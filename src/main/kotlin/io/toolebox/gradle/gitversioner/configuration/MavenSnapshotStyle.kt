package io.toolebox.gradle.gitversioner.configuration

import org.gradle.api.Action

class MavenSnapshotQualifier(action: Action<MavenSnapshotQualifier>) {

    var enabled = false

    init {
        action.execute(this)
    }
}
