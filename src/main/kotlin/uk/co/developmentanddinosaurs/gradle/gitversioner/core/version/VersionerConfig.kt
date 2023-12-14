package uk.co.developmentanddinosaurs.gradle.gitversioner.core.version

/**
 * Configuration required for versioning.
 */
interface VersionerConfig {
    /**
     * The integer to start counting from for major version increments.
     */
    val startFromMajor: Int

    /**
     * The integer to start counting from for minor version increments.
     */
    val startFromMinor: Int

    /**
     * The integer to start counting from for patch version increments.
     */
    val startFromPatch: Int

    /**
     * The string to match against for major version increments.
     */
    val matchMajor: String

    /**
     * The string to match against for minor version increments.
     */
    val matchMinor: String

    /**
     * The string to match against for patch version increments.
     */
    val matchPatch: String

    /**
     * The string pattern to use for versioning.
     */
    val pattern: String
}
