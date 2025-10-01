@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.fs.path

import klib.data.fs.Fs
import klib.data.fs.errorCode
import klib.data.fs.toIOException
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toInt
import kotlinx.io.IOException
import kotlinx.io.files.Path

private var S_IFMT = 0xf000 // fs.constants.S_IFMT
private var S_IFREG = 0x8000 // fs.constants.S_IFREG
private var S_IFDIR = 0x4000 // fs.constants.S_IFDIR
private var S_IFLNK = 0xa000 // fs.constants.S_IFLNK

public actual fun Path.metadataOrNull(): PathMetadata? {
    val pathString = toString()
    val stat = try {
        Fs.lstatSync(pathString)
    }
    catch (e: Throwable) {
        if (e.errorCode == "ENOENT") return null // "No such file or directory".
        throw IOException(e.message)
    }

    var symlinkTarget: Path? = null
    if ((stat.mode.toInt() and S_IFMT) == S_IFLNK) {
        try {
            symlinkTarget = Fs.readlinkSync(pathString).toPath()
        }
        catch (e: Throwable) {
            throw e.toIOException()
        }
    }

    return PathMetadata(
        this,
        isRegularFile = (stat.mode.toInt() and S_IFMT) == S_IFREG,
        isDirectory = (stat.mode.toInt() and S_IFMT) == S_IFDIR,
        symlinkTarget = symlinkTarget,
        size = stat.size.toInt().toLong(),
        createdAtMillis = stat.birthtimeMs.toInt().toLong(),
        lastModifiedAtMillis = stat.mtimeMs.toInt().toLong(),
        lastAccessedAtMillis = stat.atimeMs.toInt().toLong(),
    )
}
