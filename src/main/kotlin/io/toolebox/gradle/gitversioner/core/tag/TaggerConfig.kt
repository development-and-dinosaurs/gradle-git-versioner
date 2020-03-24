package io.toolebox.gradle.gitversioner.core.tag

interface TaggerConfig {
    val username: String?
    val password: String?
    val token: String?
    val strictHostChecking: Boolean
    val prefix: String
}
