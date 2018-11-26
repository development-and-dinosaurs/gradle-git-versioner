package io.toolebox.gradle.gitversioner.configuration

import io.toolebox.gradle.gitversioner.git.Git
import org.gradle.api.Action

open class VersionerPluginExtension {

    var startFrom = StartFrom(Action { })

    var mavenSnapshot = MavenSnapshot(Action { })

    var match = Match(Action { })

    var tag = Tag(Action { })

    var git = Git(Action { })

    fun startFrom(action: Action<StartFrom>) {
        startFrom = StartFrom(action)
    }

    fun mavenSnapshot(action: Action<MavenSnapshot>) {
        mavenSnapshot = MavenSnapshot(action)
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
}
