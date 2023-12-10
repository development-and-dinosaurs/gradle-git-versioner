package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration.git

import org.gradle.api.Action

class Ssh(action: Action<Ssh>) {
    var strictSsl = true

    init {
        action.execute(this)
    }
}
