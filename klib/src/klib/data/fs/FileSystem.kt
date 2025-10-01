@file:Suppress("ktlint:standard:no-wildcard-imports")

package klib.data.fs

import klib.data.BUFFER_SIZE
import klib.data.fs.model.PathMetadata
import klib.data.type.collections.iterator.AbstractClosableAbstractIterator
import klib.data.type.collections.iterator.breadthIterator
import klib.data.type.collections.iterator.coroutine.CoroutineIterator
import klib.data.type.collections.iterator.coroutine.forEach
import klib.data.type.collections.iterator.depthIterator
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemPathSeparator
import kotlinx.io.files.SystemTemporaryDirectory
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.files.FileSystem
import okio.Path.Companion.toPath

//import okio.Path.Companion.toPath

public val Regex.Companion.FILE_PATTERN: String
    get() = "^file://.*"

public val Regex.Companion.FILE: Regex
    get() = FILE_PATTERN.toRegex()

public fun String.isFileUrl(): Boolean = matches(Regex.FILE)

public expect fun String.isValidFileUrl(): Boolean

@Throws(IOException::class)
public fun FileSystem.copy(source: Path, destination: Path, bufferSize: Long = BUFFER_SIZE.toLong()) {
    source(source).use { bytesIn ->
        sink(destination).use { bytesOut ->
            val buffer = Buffer()
            while (true) {
                val bytesRead = bytesIn.readAtMostTo(buffer, bufferSize)
                if (bytesRead <= 0) break
                bytesOut.write(buffer, bytesRead)
            }
        }
    }
}

public suspend fun SequenceScope<Path>.collectRecursively(
    fileSystem: FileSystem,
    stack: ArrayDeque<Path>,
    path: Path,
    followSymlinks: Boolean,
    postorder: Boolean,
) {
    // For listRecursively, visit enclosing directory first.
    if (!postorder) {
        yield(path)
    }

    val children = fileSystem.listOrNull(path) ?: listOf()
    if (children.isNotEmpty()) {
        // Figure out if path is a symlink and detect symlink cycles.
        var symlinkPath = path
        var symlinkCount = 0
        while (true) {
            if (followSymlinks && symlinkPath in stack) throw IOException("symlink cycle at $path")
            symlinkPath = fileSystem.symlinkTarget(symlinkPath) ?: break
            symlinkCount++
        }

        // Recursively visit children.
        if (followSymlinks || symlinkCount == 0) {
            stack.addLast(symlinkPath)
            try {
                for (child in children) {
                    collectRecursively(fileSystem, stack, child, followSymlinks, postorder)
                }
            } finally {
                stack.removeLast()
            }
        }
    }

    // For deleteRecursively, visit enclosing directory last.
    if (postorder) {
        yield(path)
    }
}

/** Returns a resolved path to the symlink target, resolving it if necessary. */
@Throws(IOException::class)
internal fun FileSystem.symlinkTarget(path: Path): Path? {
    val target = metadata(path).symlinkTarget ?: return null
    return path.parent!!.div(target)
}

@Throws(IOException::class)
public fun FileSystem.commonDeleteRecursively(fileOrDirectory: Path, mustExist: Boolean) {
    val sequence = sequence {
        collectRecursively(
            fileSystem = this@commonDeleteRecursively,
            stack = ArrayDeque(),
            path = fileOrDirectory,
            followSymlinks = false,
            postorder = true,
        )
    }
    val iterator = sequence.iterator()
    while (iterator.hasNext()) {
        val toDelete = iterator.next()
        delete(toDelete, mustExist = mustExist && !iterator.hasNext())
    }
}

public fun String.pathNormalized(): String = toPath(true).toString()

public fun String.pathSegments(): List<String> = toPath().segments

public fun String.pathRelativeTo(other: String): String = toPath().relativeTo(other.toPath()).toString()

public fun String.pathResolve(child: String): String = toPath().resolve(child.toPath()).toString()

public fun String.pathResolveToRoot(
    fromRootPath: String,
    toRootPath: String,
): String = toRootPath.pathResolve(pathRelativeTo(fromRootPath))

public fun String.pathIsRoot(): Boolean = toPath().parent == null

public fun String.pathRoot(): String? = toPath().root?.toString()

public fun String.pathParent(): String? = toPath().parent?.toString()

public fun String.pathExtension(): String? = substringAfterLast(".", "").ifEmpty { null }

public fun Iterator<PathMetadata>.traverser(
    depth: Int = 0,
    followSymlinks: Boolean = false,
    depthFirst: Boolean = false,
): Iterator<PathMetadata> {
    val transform: Iterator<PathMetadata>.(Int, PathMetadata) -> Iterator<PathMetadata>? = { i, v ->
        if ((depth == -1 || i < depth) && (v.isDirectory || (v.isSymbolicLink && followSymlinks)))
            v.path.fsPathIterator()
        else null
    }

    return if (depthFirst) depthIterator(transform) else breadthIterator(transform)
}

public val SYSTEM_TEMPORARY_DIRECTORY: String = SystemTemporaryDirectory.toString()
public val SYSTEM_PATH_SEPARATOR: Char = SystemPathSeparator

public fun String.fsPathReal(): String = SystemFileSystem.canonicalize(toPath()).toString()

public fun String.fsPathExists(): Boolean = SystemFileSystem.exists(Path(this))

public fun String.fsPathMetadata(): PathMetadata = PathMetadata(this)

public fun String.fsPathSymlink(): String? = SystemFileSystem.metadataOrNull(Path(this))?.symlinkTarget?.toString()

public fun String.fsPathIsRelative(): Boolean = toPath().isRelative

public fun String.fsPathIsAbsolute(): Boolean = toPath().isAbsolute

public fun String.fsPathAbsolute(): String = toPath()
    .let {
        when {
            it.isAbsolute -> this
            else -> {
                val currentDir = "".toPath()
                SystemFileSystem.canonicalize(currentDir) / (this)
            }
        }
    }.toString()

private fun String.fsPathIterator(): Iterator<PathMetadata> =
    SystemFileSystem.list(Path(this)).map { PathMetadata(it.toString()) }.iterator()

public fun String.fsPathTraverser(
    depth: Int = 0,
    followSymlinks: Boolean = false,
    depthFirst: Boolean = false,
): Iterator<PathMetadata> = fsPathIterator().traverser(depth, followSymlinks, depthFirst)

public fun String.fsPathCreateDirectories(mustCreate: Boolean = false): Unit =
    SystemFileSystem.createDirectories(Path(this), mustCreate)

public fun String.fsPathCreateSymlink(target: String): Unit = SystemFileSystem.createSymlink(toPath(), target.toPath())

public fun String.fsPathMove(destination: String): Unit =
    SystemFileSystem.atomicMove(Path(this), Path(destination))

public fun String.fsPathCopy(destination: String, bufferSize: Long = BUFFER_SIZE.toLong()): Unit =
    SystemFileSystem.copy(Path(this), Path(destination), bufferSize)

public fun String.fsPathDelete(mustExist: Boolean = true): Unit = SystemFileSystem.delete(Path(this), mustExist)

public fun String.fsPathRead(bufferSize: Int = BUFFER_SIZE): AbstractClosableAbstractIterator<ByteArray> =
    SystemFileSystem.source(Path(this)).iterator(bufferSize)

public fun String.fsPathWrite(
    data: Iterator<ByteArray>,
    append: Boolean = false
): Unit = SystemFileSystem.sink(Path(this), append).use { sink ->
    val buffer = Buffer()
    data.forEach { bytes ->
        buffer.write(bytes)
        sink.write(buffer, bytes.size.toLong())
        sink.flush()
        buffer.clear()
    }
}

public suspend fun String.fsPathWrite(
    data: CoroutineIterator<ByteArray>,
    append: Boolean = false,
): Unit = SystemFileSystem.sink(Path(this), append).use { sink ->
    val buffer = Buffer()
    data.forEach { bytes ->
        buffer.write(bytes)
        sink.write(buffer, bytes.size.toLong())
        sink.flush()
        buffer.clear()
    }
}
