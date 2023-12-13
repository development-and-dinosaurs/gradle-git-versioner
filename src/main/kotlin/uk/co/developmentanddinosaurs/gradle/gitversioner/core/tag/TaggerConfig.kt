package uk.co.developmentanddinosaurs.gradle.gitversioner.core.tag

/**
 * Configuration required for version tagging.
 */
interface TaggerConfig {
    /**
     * The git username to authenticate the tagging action with.
     *
     * Required when using username/password authentication.
     */
    val username: String?

    /**
     * The git password to use for tagging.
     *
     * Required when using username/password authentication.
     */
    val password: String?

    /**
     * The git token to use for tagging.
     *
     * Required when using token based authentication.
     */
    val token: String?

    /**
     * Whether to perform verification of the specified host public key.
     */
    val strictHostChecking: Boolean

    /**
     * Tagging prefix to use before the version number, e.g. 'v'.
     */
    val prefix: String

    /**
     * Whether to use the full commit message of the latest commit as the tag message.
     */
    val useCommitMessage: Boolean
}
