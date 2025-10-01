package klib.data.fs.path

import java.nio.file.Path
import java.nio.file.Paths

public actual fun kotlinx.io.files.Path.metadataOrNull(): PathMetadata? {
    val file = toJavaPath().toFile()
    val isRegularFile = file.isFile
    val isDirectory = file.isDirectory
    val lastModifiedAtMillis = file.lastModified()
    val size = file.length()

    if (!isRegularFile &&
        !isDirectory &&
        lastModifiedAtMillis == 0L &&
        size == 0L &&
        !file.exists()
    ) {
        return null
    }

    return PathMetadata(
        this,
        isRegularFile,
        isDirectory,
        null,
        size,
        null,
        lastModifiedAtMillis,
        null,
    )
}

public fun kotlinx.io.files.Path.toJavaPath(): Path = Paths.get(toString())

public fun Path.toKotlinPath(): kotlinx.io.files.Path = kotlinx.io.files.Path(toString())
