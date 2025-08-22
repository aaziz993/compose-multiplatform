package gradle.api.file

import java.io.File
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemLocationProperty
import org.gradle.kotlin.dsl.assign

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(file: File?): Unit? =
    file?.let(::assign)
