package klib.data.network.ftp.client

import klib.data.BUFFER_SIZE
import klib.data.filesystem.path.PathMetadata
import klib.data.filesystem.path.resolveToRoot
import klib.data.filesystem.path.write
import klib.data.network.ftp.client.model.FtpClientConfig
import klib.data.network.ftp.client.model.FtpHost
import klib.data.type.collections.iterator.AbstractClosableAbstractIterator
import kotlinx.io.files.Path

@OptIn(ExperimentalStdlibApi::class)
public abstract class AbstractFtpClient(
    protected val host: FtpHost,
    protected val config: FtpClientConfig,
) : AutoCloseable {

    public abstract var implicit: Boolean
    public abstract var utf8: Boolean
    public abstract var passive: Boolean
    public abstract val isConnected: Boolean
    public abstract var privateData: Boolean
    public abstract fun connect()
    public abstract fun login()
    public abstract fun metadata(path: Path): PathMetadata

    protected abstract fun list(path: Path? = null): Collection<PathMetadata>

    public fun listRecursively(
        path: Path,
        followSymlinks: Boolean = false,
    ): Sequence<PathMetadata> = metadata(path).listRecursively(
        ::list,
        followSymlinks,
    )

    public abstract fun createDirectory(path: Path): Boolean

    public abstract fun createSymlink(source: Path, destination: Path): Boolean

    public abstract fun move(source: Path, destination: Path): Boolean

    public abstract fun copyFile(source: Path, destination: Path, append: Boolean = true): Boolean

    public fun copyDirectory(
        source: Path,
        destination: Path,
        followSymlinks: Boolean = false,
        append: Boolean = true,
    ): Unit = listRecursively(source, followSymlinks).forEach { metadata ->

        val path = metadata.path.resolveToRoot(source, destination)
        when {
            metadata.isRegularFile -> copyFile(metadata.path, path, append)
            metadata.isDirectory -> createDirectory(path)

            else -> Unit
        }
    }

    public fun copy(
        source: Path,
        destination: Path,
        followSymlinks: Boolean = false,
    ): Unit = metadata(source).let { metadata ->
        when {
            metadata.isRegularFile -> copyFile(source, destination)
            metadata.isDirectory -> copyDirectory(source, destination, followSymlinks)

            else -> Unit
        }
    }

    public abstract fun delete(path: Path): Boolean

    public abstract fun readFile(
        path: Path,
        bufferSize: Int = BUFFER_SIZE,
    ): AbstractClosableAbstractIterator<ByteArray>

    public fun readBytes(
        path: Path,
        bufferSize: Int = BUFFER_SIZE
    ): ByteArray = readFile(path, bufferSize).asSequence().flatMap(ByteArray::asSequence).toList()
        .toByteArray()

    public fun copyFileToLocal(
        source: Path,
        destination: Path,
        bufferSize: Int = BUFFER_SIZE,
        append: Boolean = false
    ): Unit = destination.write(readFile(source, bufferSize), append)

    public abstract fun writeFile(
        path: Path,
        data: Iterator<ByteArray>,
        append: Boolean = false,
    ): Boolean
}
