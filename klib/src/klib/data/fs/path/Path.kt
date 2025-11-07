@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.fs.path

import klib.data.BUFFER_SIZE
import klib.data.fs.canonicalize
import klib.data.fs.copy
import klib.data.fs.createSymlink
import klib.data.fs.iterator
import klib.data.type.collections.iterator.AbstractClosableAbstractIterator
import klib.data.type.collections.iterator.coroutine.CoroutineIterator
import klib.data.type.collections.iterator.coroutine.forEach
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import okio.Path.Companion.toPath as toOkioPath

public val Path.segments: List<String>
    get() = toString().toOkioPath().segments

public val Path.root: Path?
    get() = toString().toOkioPath().root?.toString()?.let(::Path)

public val Path.isRoot: Boolean
    get() = toString().toOkioPath().isRoot

public val Path.volumeLetter: Char?
    get() = toString().toOkioPath().volumeLetter

public val Path.isRelative: Boolean
    get() = !isAbsolute

public val Path.extension: String?
    get() = toString().substringAfterLast(".", "").ifEmpty { null }

public fun String.toPathOrNull(vararg parts: String): Path? =
    try {
        Path(this, *parts)
    }
    catch (_: IllegalArgumentException) {
        null
    }

public fun String.toPath(normalize: Boolean = false): Path =
    Path(toOkioPath(normalize).toString())

public operator fun Path.div(other: Path): Path =
    toString().toOkioPath().div(other.toString()).toString().toPath()

public expect fun Path.metadataOrNull(): PathMetadata?

@Throws(IOException::class)
public fun Path.metadata(): PathMetadata =
    metadataOrNull() ?: throw FileNotFoundException("no such file: $this")

public fun Path.exists(): Boolean = SystemFileSystem.exists(this)

/** Returns a resolved path to the symlink target, resolving it if necessary. */
@Throws(IOException::class)
public fun Path.symlinkTarget(): Path? {
    val target = metadata().symlinkTarget ?: return null
    return parent!! / target
}

public fun Path.absolute(): Path =
    when {
        isAbsolute -> this
        else -> {
            val currentDir = "".toPath()
            SystemFileSystem.canonicalize(currentDir) / this
        }
    }

public fun Path.canonical(): Path = SystemFileSystem.canonicalize(this)

public fun Path.list(): Collection<Path> = SystemFileSystem.list(this)

public fun Path.listRecursively(followSymlinks: Boolean = false): Sequence<PathMetadata> =
    metadata().listRecursively(
        {
            SystemFileSystem.list(this).map(Path::metadata)
        },
        followSymlinks,
    )

public fun Path.createDirectories(mustCreate: Boolean = false): Unit =
    SystemFileSystem.createDirectories(this, mustCreate)

public fun Path.createSymlink(target: String): Unit =
    SystemFileSystem.createSymlink(this, target.toPath())

public fun Path.move(destination: String): Unit =
    SystemFileSystem.atomicMove(this, destination.toPath())

public fun Path.copy(destination: String, bufferSize: Long = BUFFER_SIZE.toLong()): Unit =
    SystemFileSystem.copy(this, destination.toPath(), bufferSize)

public fun Path.delete(mustExist: Boolean = true): Unit = SystemFileSystem.delete(this, mustExist)

public fun Path.deleteRecursively(mustExist: Boolean = true): Unit =
    metadata().deleteRecursively(
        { SystemFileSystem.list(this).map(Path::metadata) },
        mustExist,
        Path::delete,
    )

public fun Path.read(bufferSize: Int = BUFFER_SIZE): AbstractClosableAbstractIterator<ByteArray> =
    SystemFileSystem.source(this).iterator(bufferSize)

public fun Path.write(
    data: Iterator<ByteArray>,
    append: Boolean = false
): Unit = SystemFileSystem.sink(this, append).use { sink ->
    val buffer = Buffer()
    data.forEach { bytes ->
        buffer.write(bytes)
        sink.write(buffer, bytes.size.toLong())
        sink.flush()
        buffer.clear()
    }
}

public suspend fun Path.write(
    data: CoroutineIterator<ByteArray>,
    append: Boolean = false,
): Unit = SystemFileSystem.sink(this, append).use { sink ->
    val buffer = Buffer()
    data.forEach { bytes ->
        buffer.write(bytes)
        sink.write(buffer, bytes.size.toLong())
        sink.flush()
        buffer.clear()
    }
}
