package klib.data.filesystem.path

import kotlin.reflect.KClass
import kotlinx.io.IOException
import kotlinx.io.files.Path

public data class PathMetadata(
    public val path: Path,
    public val isRegularFile: Boolean,
    public val isDirectory: Boolean,
    public val symlinkTarget: Path?,
    public val size: Long?,
    public val createdAtMillis: Long? = null,
    public val lastModifiedAtMillis: Long? = null,
    public val lastAccessedAtMillis: Long? = null,
    public val extras: Map<KClass<*>, Any> = emptyMap(),
) {

    public val isSymlink: Boolean = symlinkTarget != null

    /** Returns a resolved path to the symlink target, resolving it if necessary. */
    @Throws(IOException::class)
    public fun symlinkPath(): Path? {
        val target = symlinkTarget ?: return null
        return path.parent!! / target
    }

    @Throws(IOException::class)
    public fun listRecursively(
        children: Path.() -> Collection<PathMetadata>,
        followSymlinks: Boolean = false
    ): Sequence<PathMetadata> =
        sequence {
            val stack = ArrayDeque<Path>()
            stack.addLast(path)
            for (child in children(path))
                collectRecursively(
                    children,
                    stack,
                    child,
                    followSymlinks,
                    false,
                )
        }

    @Throws(IOException::class)
    public fun deleteRecursively(
        children: Path.() -> Collection<PathMetadata>,
        mustExist: Boolean = true,
        delete: Path.() -> Unit,
    ) {
        val sequence = sequence {
            collectRecursively(
                children,
                ArrayDeque(),
                this@PathMetadata,
                followSymlinks = false,
                postorder = true,
            )
        }
        val iterator = sequence.iterator()
        while (iterator.hasNext()) {
            val toDelete = iterator.next()
            toDelete.path.delete(mustExist && !iterator.hasNext())
        }
    }

    private suspend fun SequenceScope<PathMetadata>.collectRecursively(
        children: Path.() -> Collection<PathMetadata>,
        stack: ArrayDeque<Path>,
        metadata: PathMetadata,
        followSymlinks: Boolean,
        postorder: Boolean,
    ) {
        // For listRecursively, visit enclosing directory first.
        if (!postorder) {
            yield(metadata)
        }

        val paths = metadata.path.children()
        if (paths.isNotEmpty()) {
            // Figure out if path is a symlink and detect symlink cycles.
            var symlinkPath = metadata.path
            var symlinkCount = 0
            while (true) {
                if (followSymlinks && symlinkPath in stack) throw IOException("symlink cycle at $metadata")
                symlinkPath = metadata.symlinkPath() ?: break
                symlinkCount++
            }

            // Recursively visit children.
            if (followSymlinks || symlinkCount == 0) {
                stack.addLast(symlinkPath)
                try {
                    for (child in paths) {
                        collectRecursively(children, stack, child, followSymlinks, postorder)
                    }
                }
                finally {
                    stack.removeLast()
                }
            }
        }

        // For deleteRecursively, visit enclosing directory last.
        if (postorder) {
            yield(metadata)
        }
    }
}



