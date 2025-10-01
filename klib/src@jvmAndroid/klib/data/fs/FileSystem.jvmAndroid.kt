package klib.data.fs

import java.net.URI
import java.nio.file.Files
import klib.data.fs.path.toJavaPath
import klib.data.fs.path.toPath
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path

public actual fun String.isValidFileUrl(): Boolean = try {
    val uri = URI(this)
    uri.scheme == "file" && uri.isAbsolute && uri.path != null
}
catch (e: Exception) {
    false
}

public actual fun FileSystem.canonicalize(path: Path): Path {
    val canonicalFile = path.toJavaPath().toFile().canonicalFile
    if (!canonicalFile.exists()) throw FileNotFoundException("no such file")
    return canonicalFile.canonicalPath.toPath()
}

public actual fun FileSystem.createSymlink(source: Path, destination: Path) {
    Files.createSymbolicLink(source.toJavaPath(), destination.toJavaPath())
}
