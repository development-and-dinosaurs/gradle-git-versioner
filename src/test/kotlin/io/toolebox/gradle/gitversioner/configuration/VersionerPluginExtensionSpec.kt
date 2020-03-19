package io.toolebox.gradle.gitversioner.configuration

import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.toolebox.gradle.gitversioner.configuration.git.Authentication
import io.toolebox.gradle.gitversioner.configuration.git.Git
import io.toolebox.gradle.gitversioner.configuration.git.Https
import io.toolebox.gradle.gitversioner.configuration.git.Ssh
import org.gradle.api.Action

class VersionerPluginExtensionSpec : FreeSpec() {
    init {
        "starts from" - {
            "default" - {
                "major 0 when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom.major shouldBe 0
                }
                "minor 0 when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom.minor shouldBe 0
                }
                "patch 0 when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom.patch shouldBe 0
                }
            }
            "configured using" - {
                "major when specified" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom(Action { it.major = 1 })

                    extension.startFrom.major shouldBe 1
                }
                "minor when specified" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom(Action { it.minor = 1 })

                    extension.startFrom.minor shouldBe 1
                }
                "patch when specified" {
                    val extension = VersionerPluginExtension()

                    extension.startFrom(Action { it.patch = 1 })

                    extension.startFrom.patch shouldBe 1
                }
            }
        }
        "matches" - {
            "default" - {
                "major [major] when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.match.major shouldBe "[major]"
                }
                "minor [minor] when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.match.minor shouldBe "[minor]"
                }
                "patch [patch] when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.match.patch shouldBe "[patch]"
                }
            }
            "configured using" - {
                "major when specified" {
                    val extension = VersionerPluginExtension()

                    extension.match(Action { it.major = "[maj]" })

                    extension.match.major shouldBe "[maj]"
                }
                "minor when specified" {
                    val extension = VersionerPluginExtension()

                    extension.match(Action { it.minor = "[min]" })

                    extension.match.minor shouldBe "[min]"
                }
                "patch when specified" {
                    val extension = VersionerPluginExtension()

                    extension.match(Action { it.patch = "[pat]" })

                    extension.match.patch shouldBe "[pat]"
                }
            }
        }
        "tags" - {
            "default" - {
                "prefix as v when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.tag.prefix shouldBe "v"
                }
            }
            "configured using" - {
                "prefix when specified" {
                    val extension = VersionerPluginExtension()

                    extension.tag(Action { it.prefix = "x" })

                    extension.tag.prefix shouldBe "x"
                }
            }
        }
        "git" - {
            "authentication" - {
                "https" - {
                    "default" - {
                        "null username when not otherwise specified" {
                            val extension = VersionerPluginExtension()

                            extension.git.authentication.https.username shouldBe null
                        }
                        "null password when not otherwise specified" {
                            val extension = VersionerPluginExtension()

                            extension.git.authentication.https.password shouldBe null
                        }
                        "null token when not otherwise specified" {
                            val extension = VersionerPluginExtension()

                            extension.git.authentication.https.token shouldBe null
                        }
                    }
                    "configured using" - {
                        "username when specified" {
                            val extension = VersionerPluginExtension()

                            extension.git(gitConfigHttps(Action { it.username = "username" }))

                            extension.git.authentication.https.username shouldBe "username"
                        }
                        "password when specified" {
                            val extension = VersionerPluginExtension()

                            extension.git(gitConfigHttps(Action { it.password = "password" }))

                            extension.git.authentication.https.password shouldBe "password"
                        }
                        "token when specified" {
                            val extension = VersionerPluginExtension()

                            extension.git(gitConfigHttps(Action { it.token = "token" }))

                            extension.git.authentication.https.token shouldBe "token"
                        }
                    }
                }
                "ssh" - {
                    "default" - {
                        "strictSsl true when not otherwise specified" {
                            val extension = VersionerPluginExtension()

                            extension.git.authentication.ssh.strictSsl shouldBe true
                        }
                    }
                    "configured using" - {
                        "strictSsl when specified" {
                            val extension = VersionerPluginExtension()

                            extension.git(gitConfigSsh(Action { it.strictSsl = false }))

                            extension.git.authentication.ssh.strictSsl shouldBe false
                        }
                    }
                }
            }
        }
        "pattern" - {
            "default" - {
                "pattern %M.%m.%p(.%c) when not otherwise specified" {
                    val extension = VersionerPluginExtension()

                    extension.pattern.pattern shouldBe "%M.%m.%p(.%c)"
                }
            }
            "configured using" - {
                "pattern when specified" {
                    val extension = VersionerPluginExtension()

                    extension.pattern(Action { it.pattern = "%M" })

                    extension.pattern.pattern shouldBe "%M"
                }
            }
        }
    }

    private fun gitConfigHttps(action: Action<Https>): Action<Git> =
        Action { it.authentication =
            Authentication(
                Action {
                    it.https =
                        Https(
                            action
                        )
                })
        }

    private fun gitConfigSsh(action: Action<Ssh>): Action<Git> =
        Action { it.authentication =
            Authentication(
                Action {
                    it.ssh =
                        Ssh(
                            action
                        )
                })
        }
}
