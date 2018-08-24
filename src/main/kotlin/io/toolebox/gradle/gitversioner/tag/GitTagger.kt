package io.toolebox.gradle.gitversioner.tag

import com.jcraft.jsch.JSch
import io.toolebox.gradle.gitversioner.configuration.Tag
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.Project
import java.io.File

class GitTagger(private val project: Project) {

    fun tag(version: String, tag: Tag, git: io.toolebox.gradle.gitversioner.git.Git) {
        var credentialsProvider = CredentialsProvider.getDefault()
        if (git.authentication.ssh.strictSsl) {
            JSch.setConfig("StrictHostKeyChecking", "yes");
        } else {
            JSch.setConfig("StrictHostKeyChecking", "no");
        }
        if (git.authentication.https.username != null) {
            val username = git.authentication.https.username
            val password = git.authentication.https.password
            credentialsProvider = UsernamePasswordCredentialsProvider(username, password)
        }
        if (git.authentication.https.token != null) {
            val username = git.authentication.https.token
            credentialsProvider = UsernamePasswordCredentialsProvider(username, "")
        }
        val egit = Git.open(File("${project.rootDir}/.git"))
        val prefixedVersion = "${tag.prefix}$version"
        egit.tag().setName(prefixedVersion).call()
        egit.push().add(prefixedVersion).setCredentialsProvider(credentialsProvider).call()
    }

}
