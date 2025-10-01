package klib.data.fs

import klib.data.fs.path.toPath
import kotlinx.io.IOException
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import web.url.URL

public actual fun String.isValidFileUrl(): Boolean = try {
    val uri = URL(this) // Use appropriate JS URI parsing method
    uri.protocol == "file:"
}
catch (_: Exception) {
    false
}

public actual fun FileSystem.canonicalize(path: Path): Path {
    try {
        val canonicalPath = realpathSync(path.toString())
        return canonicalPath.toPath()
    }
    catch (e: Throwable) {
        throw e.toIOException()
    }
}

public actual fun FileSystem.createSymlink(source: Path, destination: Path) {
    if (source.parent == null || !exists(source.parent!!)) {
        throw IOException("parent directory does not exist: ${source.parent}")
    }

    if (exists(source)) {
        throw IOException("already exists: $source")
    }

    symlinkSync(destination.toString(), source.toString())
}

internal val Throwable.errorCode
    get() = if (message?.startsWith("Error: ") == true)
        message!!.removePrefix("Error: ").substringBefore(":")
    else null

internal fun Throwable.toIOException(): IOException {
    return when (errorCode) {
        "ENOENT" -> FileNotFoundException(message)
        else -> IOException(message)
    }
}
