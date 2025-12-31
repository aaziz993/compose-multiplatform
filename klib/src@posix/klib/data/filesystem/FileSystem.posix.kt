@file:OptIn(ExperimentalForeignApi::class)

package klib.data.filesystem

import klib.data.filesystem.path.toPath
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.readString
import platform.posix.PATH_MAX
import platform.posix.S_IFLNK
import platform.posix.S_IFMT
import platform.posix.errno
import platform.posix.free
import platform.posix.readlink
import platform.posix.realpath
import platform.posix.stat
import platform.posix.symlink

public actual fun FileSystem.canonicalize(path: Path): Path {
    // Note that realpath() fails if the file doesn't exist.
    val fullpath = realpath(path.toString(), null)
        ?: throw errnoToIOException(errno)
    try {
        return Buffer().writeNullTerminated(fullpath).readString().toPath(normalize = true)
    }
    finally {
        free(fullpath)
    }
}

public actual fun FileSystem.createSymlink(source: Path, destination: Path) {
    if (source.parent == null || !exists(source.parent!!)) {
        throw IOException("parent directory does not exist: ${source.parent}")
    }

    if (exists(source)) {
        throw IOException("already exists: $source")
    }

    val result = symlink(destination.toString(), source.toString())
    if (result != 0) {
        throw errnoToIOException(errno)
    }
}

@OptIn(UnsafeNumber::class)
internal fun symlinkTarget(stat: stat, path: Path): Path? {
    if (stat.st_mode.toInt() and S_IFMT != S_IFLNK) return null

    // `path` is a symlink, let's resolve its target.
    memScoped {
        val buffer = allocArray<ByteVar>(PATH_MAX)
        val byteCount = readlink(path.toString(), buffer, PATH_MAX.convert())
        if (byteCount.convert<Int>() == -1) {
            throw errnoToIOException(errno)
        }
        return buffer.toKString().toPath()
    }
}


