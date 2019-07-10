package io.toolebox.gradle.gitversioner.configuration

import io.toolebox.gradle.gitversioner.git.Git
import org.gradle.api.Action

open class VersionerPluginExtension {

    var startFrom = StartFrom(Action { })

    var match = Match(Action { })

    var tag = Tag(Action { })

    var git = Git(Action { })

    var pattern = Pattern(Action { })

    fun startFrom(action: Action<StartFrom>) {
        startFrom = StartFrom(action)
    }

    fun match(action: Action<Match>) {
        match = Match(action)
    }

    fun tag(action: Action<Tag>) {
        tag = Tag(action)
    }

    fun git(action: Action<Git>) {
        git = Git(action)
    }

    fun pattern(action: Action<Pattern>) {
        pattern = Pattern(action)
    }
}
