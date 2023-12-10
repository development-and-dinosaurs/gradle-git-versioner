package uk.co.developmentanddinosaurs.gradle.gitversioner.core.version

import org.eclipse.jgit.api.Git
import java.io.File

class Versioner(private val gitFolder: File) {
    fun version(config: VersionerConfig): Version {
        var major = config.startFromMajor
        var minor = config.startFromMinor
        var patch = config.startFromPatch
        var commit = 0

        val git = Git.open(gitFolder)

        val branch = git.repository.branch
        val hash = git.repository.findRef("HEAD").objectId.name

        val all = git.log().call()
        all.reversed().forEach {
            when {
                it.fullMessage.contains(config.matchMajor) -> {
                    major++
                    minor = 0
                    patch = 0
                    commit = 0
                }
                it.fullMessage.contains(config.matchMinor) -> {
                    minor++
                    patch = 0
                    commit = 0
                }
                it.fullMessage.contains(config.matchPatch) -> {
                    patch++
                    commit = 0
                }
                else -> commit++
            }
        }

        return Version(major, minor, patch, commit, branch, hash)
    }
}
