package io.toolebox.gradle.gitversioner

import java.io.File
import java.io.InputStream

fun File.withContents(inputStream: InputStream): File {
    this.writeBytes(inputStream.readBytes())
    return this
}
