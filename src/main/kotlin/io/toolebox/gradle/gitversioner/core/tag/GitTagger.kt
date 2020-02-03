package io.toolebox.gradle.gitversioner.core.tag

import com.jcraft.jsch.JSch
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File
import org.eclipse.jgit.api.Git as JGit

class GitTagger(private val gitFolder: File, private val config: TaggerConfig) {

    fun tag(version: String) {
        configureHostChecking(config)
        val credentialsProvider = createCredentialsProvider(config)
        val jGit = JGit.open(gitFolder)
        val prefixedVersion = "${config.prefix}$version"
        jGit.tag().setName(prefixedVersion).call()
        jGit.push().add(prefixedVersion).setCredentialsProvider(credentialsProvider).call()
    }

    private fun configureHostChecking(config: TaggerConfig) {
        if (config.strictHostChecking) {
            JSch.setConfig("StrictHostKeyChecking", "yes");
        } else {
            JSch.setConfig("StrictHostKeyChecking", "no");
        }
    }

    private fun createCredentialsProvider(config: TaggerConfig): CredentialsProvider? {
        with(config) {
            return when {
                username != null -> UsernamePasswordCredentialsProvider(username, password)
                token != null -> UsernamePasswordCredentialsProvider(token, "")
                else -> CredentialsProvider.getDefault()
            }
        }
    }
}
