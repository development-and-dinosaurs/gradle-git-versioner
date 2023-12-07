package io.toolebox.gradle.gitversioner.configuration

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ConfigurationExceptionSpec : StringSpec() {
    init {
        "Has correct message" {
            val message = "my message"
            try {
                throw ConfigurationException(message)
            } catch (e: ConfigurationException) {
                e.message shouldBe message
            }
        }
    }
}
