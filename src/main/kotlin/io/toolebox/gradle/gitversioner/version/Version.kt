package io.toolebox.gradle.gitversioner.version

private const val EMPTY_STRING = ""

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val commit: Int,
    val branch: String,
    val hash: String
) {
    fun print(pattern: String): String {
        val filledVersion = pattern
            .replace("%M", major.toString())
            .replace("%m", minor.toString())
            .replace("%p", patch.toString())
            .replace("%c", commit.toString())
            .replace("%b", branch)
            .replace("%H", hash)
            .replace("%h", hash.substring(0, 7))
        return if (commit != 0)
            removeParentheses(filledVersion)
        else
            removeCommitConditionals(filledVersion)
    }

    private fun removeCommitConditionals(version: String) =
        version.replace(Regex("\\(.*\\)"), EMPTY_STRING)

    private fun removeParentheses(version: String) =
        version
            .replace("(", EMPTY_STRING)
            .replace(")", EMPTY_STRING)
}
