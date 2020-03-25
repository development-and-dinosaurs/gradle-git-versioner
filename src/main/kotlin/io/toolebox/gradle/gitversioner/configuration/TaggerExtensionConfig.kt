package io.toolebox.gradle.gitversioner.configuration

import io.toolebox.gradle.gitversioner.core.tag.TaggerConfig

class TaggerExtensionConfig(extension: VersionerPluginExtension) : TaggerConfig {
    override val username by lazy { extension.git.authentication.https.username }
    override val password by lazy { extension.git.authentication.https.password }
    override val token by lazy { extension.git.authentication.https.token }
    override val strictHostChecking by lazy { extension.git.authentication.ssh.strictSsl }
    override val prefix by lazy { extension.tag.prefix }
    override val useCommitMessage by lazy { extension.tag.useCommitMessage }
}
