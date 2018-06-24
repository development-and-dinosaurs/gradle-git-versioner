package io.toolebox.gradle.gitversioner

import io.kotlintest.specs.FreeSpec
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Action

class VersionerPluginExtensionTest : FreeSpec() {
    init {
        "Version Plugin Extension" - {
            "starts from" - {
                "major 0 when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    assertThat(extension.startFrom.major).isEqualTo(0)
                }
                "minor 0 when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    assertThat(extension.startFrom.minor).isEqualTo(0)
                }
                "patch 0 when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    assertThat(extension.startFrom.patch).isEqualTo(0)
                }
                "specified major when provided" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom(Action { it.major = 1 })

                    assertThat(extension.startFrom.major).isEqualTo(1)
                }
                "specified minor when provided" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom(Action { it.minor = 1 })

                    assertThat(extension.startFrom.minor).isEqualTo(1)
                }
                "specified patch when provided" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom(Action { it.patch = 1 })

                    assertThat(extension.startFrom.patch).isEqualTo(1)
                }
            }
            "matches against" - {
                "major \\[major\\] when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    assertThat(extension.match.major).isEqualTo("\\[major\\]")
                }
                "minor \\[minor\\] when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    assertThat(extension.match.minor).isEqualTo("\\[minor\\]")
                }
                "patch \\[patch\\] when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    assertThat(extension.match.patch).isEqualTo("\\[patch\\]")
                }
                "specified major when provided" {
                    val extension = VersionerPluginExtension()

                    extension.match(Action { it.major = "\\[maj\\]" })

                    assertThat(extension.match.major).isEqualTo("\\[maj\\]")
                }
                "specified minor when provided" {
                    val extension = VersionerPluginExtension()

                    extension.match(Action { it.minor = "\\[min\\]" })

                    assertThat(extension.match.minor).isEqualTo("\\[min\\]")
                }
                "specified patch when provided" {
                    val extension = VersionerPluginExtension()

                    extension.match(Action { it.patch = "\\[pat\\]" })

                    assertThat(extension.match.patch).isEqualTo("\\[pat\\]")
                }
            }
        }
    }
}
