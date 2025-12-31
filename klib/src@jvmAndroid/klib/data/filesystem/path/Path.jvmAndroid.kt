package klib.data.filesystem.path

import kotlinx.io.files.Path
import java.nio.file.Path as JavaPath
import java.nio.file.Paths

public actual fun Path.metadataOrNull(): PathMetadata? {
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

public fun Path.toJavaPath(): JavaPath = Paths.get(toString())

public fun JavaPath.toKotlinPath(): Path = Path(toString())
