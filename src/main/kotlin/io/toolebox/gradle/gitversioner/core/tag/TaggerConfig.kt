package io.toolebox.gradle.gitversioner.core.tag

data class TaggerConfig(
    val username: String? = null,
    val password: String? = null,
    val token: String? = null,
    val strictHostChecking: Boolean = true,
    val prefix: String = "v"
)
