package klib.data.fs.path

import klib.data.fs.errnoToIOException
import klib.data.fs.symlinkTarget
import klib.data.type.primitive.epochMillis
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.ENOENT
import platform.posix.S_IFDIR
import platform.posix.S_IFMT
import platform.posix.S_IFREG
import platform.posix.errno
import platform.posix.lstat
import platform.posix.stat

@OptIn(ExperimentalForeignApi::class)
public actual fun Path.metadataOrNull(): PathMetadata? {
    return memScoped {
        val stat = alloc<stat>()
        if (lstat(toString(), stat.ptr) != 0) {
            if (errno == ENOENT) return null
            throw errnoToIOException(errno)
        }
        return@memScoped PathMetadata(
            this@metadataOrNull,
            stat.st_mode.toInt() and S_IFMT == S_IFREG,
            stat.st_mode.toInt() and S_IFMT == S_IFDIR,
            symlinkTarget(stat, this@metadataOrNull),
            stat.st_size,
            stat.st_ctimespec.epochMillis,
            stat.st_mtimespec.epochMillis,
            stat.st_atimespec.epochMillis,
        )
    }
}


