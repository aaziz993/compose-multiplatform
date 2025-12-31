@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.filesystem.path

import io.ktor.utils.io.core.readBytes
import klib.data.BUFFER_SIZE
import klib.data.filesystem.canonicalize
import klib.data.filesystem.copy
import klib.data.filesystem.createSymlink
import klib.data.filesystem.iterator
import klib.data.type.collections.iterator.AbstractClosableAbstractIterator
import klib.data.type.collections.iterator.coroutine.CoroutineIterator
import klib.data.type.collections.iterator.coroutine.forEach
import kotlin.jvm.JvmName
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.buffered
import kotlinx.io.bytestring.ByteString
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.okio.toOkioByteString
import okio.Path.Companion.toPath as toOkioPath

public val Path.segments: List<String>
    get() = toOkioPath().segments

public val Path.root: Path?
    get() = toOkioPath().root?.toString()?.let(::Path)

public val Path.isRoot: Boolean
    get() = toOkioPath().isRoot

public val Path.volumeLetter: Char?
    get() = toOkioPath().volumeLetter

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

public fun Path.delete(mustExist: Boolean = false): Unit = SystemFileSystem.delete(this, mustExist)

public fun Path.deleteRecursively(mustExist: Boolean = false): Unit =
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
    val bufferedSink = sink.buffered()
    data.forEach { bytes ->
        bufferedSink.write(bytes)
        sink.flush()
    }
}

public suspend fun Path.write(
    data: CoroutineIterator<ByteArray>,
    append: Boolean = false,
): Unit = SystemFileSystem.sink(this, append).use { sink ->
    val bufferedSink = sink.buffered()
    data.forEach { bytes ->
        bufferedSink.write(bytes)
        sink.flush()
    }
}

public fun Path.resolve(child: String, normalize: Boolean = false): Path =
    toOkioPath().resolve(child, normalize).toPath()

public fun Path.resolve(child: ByteString, normalize: Boolean = false): Path =
    toOkioPath().resolve(child.toOkioByteString(), normalize).toPath()

public fun Path.resolve(child: Path, normalize: Boolean = false): Path =
    toOkioPath().resolve(child.toOkioPath(), normalize).toPath()

@JvmName("resolve")
public operator fun Path.div(child: String): Path = toOkioPath().div(child).toPath()

@JvmName("resolve")
public operator fun Path.div(child: ByteString): Path = toOkioPath().div(child.toOkioByteString()).toPath()

@JvmName("resolve")
public operator fun Path.div(child: Path): Path = toOkioPath().div(child.toOkioPath()).toPath()

public fun Path.relativeTo(other: Path): Path = toOkioPath().relativeTo(other.toOkioPath()).toPath()

public fun Path.resolveToRoot(fromRootPath: Path, toRootPath: Path): Path =
    toRootPath / relativeTo(fromRootPath)

public fun Path.normalized(): Path = toOkioPath().normalized().toPath()

public fun Path.compareTo(other: Path): Int = toOkioPath().compareTo(other.toOkioPath())

public fun String.toPath(vararg parts: String): Path = Path(this, *parts)
public fun String.toPath(normalize: Boolean): Path = toOkioPath(normalize).toPath()

public fun Buffer.toPath(): Path = okio.Path(okio.Buffer().write(readBytes())).toPath()

private fun Path.toOkioPath(): okio.Path = toString().toOkioPath()
private fun okio.Path.toPath(): Path = toString().toPath()
