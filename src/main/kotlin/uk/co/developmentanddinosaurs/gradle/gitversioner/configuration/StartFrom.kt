package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import org.gradle.api.Action

class StartFrom(action: Action<StartFrom>) {
    var major = 0
    var minor = 0
    var patch = 0

    init {
        action.execute(this)
    }
}
