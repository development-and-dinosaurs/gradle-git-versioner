package io.toolebox.gradle.gitversioner

/**
 * An exception that is thrown when there is an issue with the configuration for the versioner task.
 */
class ConfigurationException(message: String) : RuntimeException(message)
