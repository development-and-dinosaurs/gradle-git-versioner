package io.toolebox.gradle.gitversioner.tag

import io.toolebox.gradle.gitversioner.configuration.Tag
import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import java.io.File

class GitTagger(private val project: Project) {

    fun tag(version: String, tag: Tag) {
        val git = Git.open(File("${project.rootDir}/.git"))
        val prefixedVersion = "${tag.prefix}$version"
        git.tag().setName(prefixedVersion).call()
        git.push().add(prefixedVersion).call()
    }

}
