package io.toolebox.gradle.gitversioner

import org.gradle.api.Action

open class VersionerPluginExtension {

    var startFrom = StartFrom(Action { it ->
        it.major = 0
        it.minor = 0
        it.patch = 0
    })

    var match = Match(Action {
        it.major = "\\[major\\]"
        it.minor = "\\[minor\\]"
        it.patch = "\\[patch\\]"
    })

    fun startFrom(action: Action<StartFrom>) {
        startFrom = StartFrom(action)
    }

    fun match(action: Action<Match>) {
        match = Match(action)
    }

}
