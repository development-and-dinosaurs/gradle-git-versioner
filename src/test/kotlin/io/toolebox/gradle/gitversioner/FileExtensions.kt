package io.toolebox.gradle.gitversioner

import java.io.File

fun File.withContents(bytes: ByteArray): File {
    this.writeBytes(bytes)
    return this
}
