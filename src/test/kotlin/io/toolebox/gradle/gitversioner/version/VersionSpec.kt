package io.toolebox.gradle.gitversioner.version

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.toolebox.gradle.gitversioner.core.version.Version

class VersionSpec : StringSpec() {
    init {
        val versionWithCommit = Version(1, 2, 3, 4, "mybranch", "myhash123")
        val versionWithoutCommit = Version(1, 2, 3, 0, "mybranch", "myhash123")

        "prints version correctly according to pattern" {
            forAll(
                row(versionWithCommit, "%M.%m.%p.%c", "1.2.3.4"),
                row(versionWithCommit, "%M.%m.%p-%c", "1.2.3-4"),
                row(versionWithCommit, "%M.%m.%p-%H", "1.2.3-myhash123"),
                row(versionWithCommit, "%M.%m.%p-%h", "1.2.3-myhash1"),
                row(versionWithCommit, "%M.%m.%p-%b", "1.2.3-mybranch"),
                row(versionWithCommit, "%M.%m.%p(-%c)", "1.2.3-4"),
                row(versionWithoutCommit, "%M.%m.%p(-%b)", "1.2.3"),
                row(versionWithCommit, "%M.%m.%p(-SNAPSHOT)", "1.2.3-SNAPSHOT"),
                row(versionWithoutCommit, "%M.%m.%p(-SNAPSHOT)", "1.2.3"),
            ) { version, pattern, output ->
                val print = version.print(pattern)
                print shouldBe output
            }
        }
    }
}
