package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import uk.co.developmentanddinosaurs.gradle.gitversioner.core.tag.TaggerConfig

/**
 * Implementation of [TaggerConfig] that uses a Gradle specific [VersionerExtension] to collect the configuration.
 */
class TaggerExtensionConfig(extension: VersionerExtension) : TaggerConfig {
    override val username by lazy { extension.git.authentication.https.username }
    override val password by lazy { extension.git.authentication.https.password }
    override val token by lazy { extension.git.authentication.https.token }
    override val strictHostChecking by lazy { extension.git.authentication.ssh.strictSsl }
    override val prefix by lazy { extension.tag.prefix }
    override val useCommitMessage by lazy { extension.tag.useCommitMessage }
}
