package uk.co.developmentanddinosaurs.gradle.gitversioner.core.version

/**
 * A calculated version based on the provided version details.
 */
data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val commit: Int,
    val branch: String,
    val hash: String,
) {
    /**
     * Print the version based on the provided pattern.
     *
     * @param pattern The pattern to use to print the version.
     * @return The version string.
     */
    fun print(pattern: String): String {
        val filledVersion =
            pattern
                .replace("%M", major.toString())
                .replace("%m", minor.toString())
                .replace("%p", patch.toString())
                .replace("%c", commit.toString())
                .replace("%b", branch)
                .replace("%H", hash)
                .replace("%h", hash.substring(0, 7))
        return if (commit != 0) {
            removeParentheses(filledVersion)
        } else {
            removeCommitConditionals(filledVersion)
        }
    }

    private fun removeCommitConditionals(version: String) = version.replace(Regex("\\(.*\\)"), "")

    private fun removeParentheses(version: String) = version.replace("(", "").replace(")", "")
}
