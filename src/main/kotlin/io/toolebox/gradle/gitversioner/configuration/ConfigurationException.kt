package io.toolebox.gradle.gitversioner.configuration

/**
 * An exception that is thrown when there is an issue with the configuration for the versioner task.
 */
class ConfigurationException(message: String) : RuntimeException(message)
