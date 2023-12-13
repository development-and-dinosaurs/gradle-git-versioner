package uk.co.developmentanddinosaurs.gradle.gitversioner.core.tag

import com.jcraft.jsch.JSch
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

/**
 * Creates and pushes git tags based on the tagging config provider.
 */
class GitTagger(private val gitFolder: File, private val config: TaggerConfig) {
    /**
     * Creates a git tag and pushes it to the remote repository.
     *
     * @param version the version to tag
     */
    fun tag(version: String) {
        configureHostChecking(config)
        val credentialsProvider = createCredentialsProvider(config)
        val prefixedVersion = "${config.prefix}$version"
        val git = Git.open(gitFolder)
        val tagCommand = git.tag().setName(prefixedVersion)
        if (config.useCommitMessage) {
            tagCommand.setMessage(getLastCommitMessage(git))
        }
        tagCommand.call()
        git.push().add(prefixedVersion).setCredentialsProvider(credentialsProvider).call()
    }

    private fun getLastCommitMessage(git: Git) = git.log().setMaxCount(1).call().iterator().next().fullMessage

    private fun configureHostChecking(config: TaggerConfig) {
        if (config.strictHostChecking) {
            JSch.setConfig("StrictHostKeyChecking", "yes")
        } else {
            JSch.setConfig("StrictHostKeyChecking", "no")
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
