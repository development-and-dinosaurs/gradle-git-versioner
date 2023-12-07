package io.toolebox.gradle.gitversioner.configuration

import org.gradle.api.Action

class Pattern(action: Action<Pattern>) {
    var pattern = "%M.%m.%p(.%c)"

    init {
        action.execute(this)
    }
}
