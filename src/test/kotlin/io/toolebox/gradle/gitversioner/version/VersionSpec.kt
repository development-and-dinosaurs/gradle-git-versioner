package io.toolebox.gradle.gitversioner.version

import io.kotlintest.data.forall
import io.kotlintest.specs.FreeSpec
import io.kotlintest.tables.row
import org.assertj.core.api.Assertions.assertThat

class VersionSpec : FreeSpec() {

    init {
        val versionWithCommit = Version(1, 2, 3, 4, "mybranch", "myhash")
        val versionWithoutCommit = Version(1, 2, 3, 0, "mybranch", "myhash")
        "Version" - {
            "prints version correctly according to pattern" {
                forall(
                    row(versionWithCommit, "%M.%m.%p.%c", "1.2.3.4"),
                    row(versionWithCommit, "%M.%m.%p-%c", "1.2.3-4"),
                    row(versionWithCommit, "%M.%m.%p-%h", "1.2.3-myhash"),
                    row(versionWithCommit, "%M.%m.%p-%b", "1.2.3-mybranch"),
                    row(versionWithCommit, "%M.%m.%p(-%c)", "1.2.3-4"),
                    row(versionWithoutCommit, "%M.%m.%p(-%b)", "1.2.3"),
                    row(versionWithCommit, "%M.%m.%p(-SNAPSHOT)", "1.2.3-SNAPSHOT"),
                    row(versionWithoutCommit, "%M.%m.%p(-SNAPSHOT)", "1.2.3")
                )
                { version, pattern, output ->
                    val print = version.print(pattern)
                    assertThat(print).isEqualTo(output)
                }
            }
        }
    }

}
