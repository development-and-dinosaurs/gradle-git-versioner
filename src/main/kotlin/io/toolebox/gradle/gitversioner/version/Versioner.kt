package io.toolebox.gradle.gitversioner.version

import io.toolebox.gradle.gitversioner.configuration.Match
import io.toolebox.gradle.gitversioner.configuration.StartFrom
import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import java.io.File

class Versioner(private val project: Project) {

    fun version(startFrom: StartFrom, match: Match): String {
        var major = startFrom.major
        var minor = startFrom.minor
        var patch = startFrom.patch
        var build = 0

        val git = Git.open(File("${project.rootDir}/.git"))
        val all = git.log().all().call()
        all.reversed().forEach {
            when {
                it.fullMessage.contains(Regex(match.major)) -> {
                    major++
                    minor = 0
                    patch = 0
                    build = 0
                }
                it.fullMessage.contains(Regex(match.minor)) -> {
                    minor++
                    patch = 0
                    build = 0
                }
                it.fullMessage.contains(Regex(match.patch)) -> {
                    patch++
                    build = 0
                }
                else -> build++
            }
        }
        return "$major.$minor.$patch${if (build == 0) "" else ".$build"}"
    }

}
