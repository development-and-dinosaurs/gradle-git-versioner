package io.toolebox.gradle.gitversioner.core.version

data class VersionerConfig(
    val startFromMajor: Int = 0,
    val startFromMinor: Int = 0,
    val startFromPatch: Int = 0,
    val matchMajor: Regex = Regex("\\[major]"),
    val matchMinor: Regex = Regex("\\[minor]"),
    val matchPatch: Regex = Regex("\\[patch]")
)
