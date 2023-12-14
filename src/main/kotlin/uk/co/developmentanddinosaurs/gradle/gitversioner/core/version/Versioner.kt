package uk.co.developmentanddinosaurs.gradle.gitversioner.core.version

import org.eclipse.jgit.api.Git
import java.io.File

/**
 * Calculates the full version of a project based on the git history provided.
 */
class Versioner(private val gitFolder: File) {
    /**
     * Calculate the version for the provided configuration.
     *
     * @param config the [VersionerConfig] to use for calculating the version
     * @return the [Version] calculated using the configuration and history
     */
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
