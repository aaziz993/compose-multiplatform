package gradle.api.file

import java.io.File
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemLocationProperty
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(file: File?): Unit? =
    file?.let(::assign)

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(provider: Provider<File?>): Unit? =
    provider.takeIf(Provider<File?>::isPresent)?.let(::assign)
