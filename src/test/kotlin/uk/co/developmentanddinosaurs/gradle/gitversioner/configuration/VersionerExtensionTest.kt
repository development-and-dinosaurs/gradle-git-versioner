package uk.co.developmentanddinosaurs.gradle.gitversioner.configuration

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.gradle.api.Project
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.version.Version
import uk.co.developmentanddinosaurs.gradle.gitversioner.core.version.Versioner

class VersionerExtensionTest : FunSpec({

    val project = mockk<Project>()
    val versioner = mockk<Versioner>()

    val extension = VersionerExtension(project, versioner)

    test("Can apply version to project") {
        val versionSlot = slot<Any>()
        every { project.version = capture(versionSlot) } answers {}
        every { versioner.version(any()) } returns Version(1, 0, 0, 0, "branch", "hash123")

        extension.apply()

        versionSlot.captured shouldBe "1.0.0"
    }
})
