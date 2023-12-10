package uk.co.developmentanddinosaurs.gradle.gitversioner.core.version

interface VersionerConfig {
    val startFromMajor: Int
    val startFromMinor: Int
    val startFromPatch: Int
    val matchMajor: String
    val matchMinor: String
    val matchPatch: String
    val pattern: String
}
