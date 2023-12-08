package uk.co.developmentanddinosaurs.gradle.gitversioner.core.tag

interface TaggerConfig {
    val username: String?
    val password: String?
    val token: String?
    val strictHostChecking: Boolean
    val prefix: String
    val useCommitMessage: Boolean
}
