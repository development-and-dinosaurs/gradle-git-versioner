package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import uk.co.developmentanddinosaurs.gradle.gitversioner.core.version.VersionerConfig

class VersionerExtensionConfig(extension: VersionerExtension) : VersionerConfig {
    override val startFromMajor by lazy { extension.startFrom.major }
    override val startFromMinor by lazy { extension.startFrom.minor }
    override val startFromPatch by lazy { extension.startFrom.patch }
    override val matchMajor by lazy { extension.match.major }
    override val matchMinor by lazy { extension.match.minor }
    override val matchPatch by lazy { extension.match.patch }
    override val pattern by lazy { extension.pattern.pattern }
}
