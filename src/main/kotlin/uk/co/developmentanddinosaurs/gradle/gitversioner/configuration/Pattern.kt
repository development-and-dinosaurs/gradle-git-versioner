package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import org.gradle.api.Action

/**
 * Configuration to specify the pattern to use for versioning.
 */
class Pattern(action: Action<Pattern>) {
    /**
     * The pattern to use for versioning.
     *
     * Default is "%M.%m.%p(.%c)"
     */
    var pattern = "%M.%m.%p(.%c)"

    init {
        action.execute(this)
    }
}
