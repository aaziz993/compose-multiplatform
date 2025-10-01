package klib.data.net.ftp.client

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.LoggerFactory
import net.schmizz.sshj.common.StreamCopier
import net.schmizz.sshj.sftp.FileAttributes
import net.schmizz.sshj.sftp.FileMode
import net.schmizz.sshj.sftp.OpenMode
import net.schmizz.sshj.sftp.RemoteFile
import net.schmizz.sshj.sftp.RemoteFile.ReadAheadRemoteFileInputStream
import net.schmizz.sshj.sftp.RemoteFile.RemoteFileOutputStream
import net.schmizz.sshj.sftp.SFTPClient
import java.io.IOException
import java.io.InputStream
import java.util.*
import klib.data.fs.path.PathMetadata
import klib.data.fs.path.toPath
import klib.data.net.ftp.client.model.FtpClientConfig
import klib.data.net.ftp.client.model.FtpHost
import klib.data.type.collections.asInputStream
import klib.data.type.collections.iterator
import klib.data.type.collections.iterator.AbstractClosableAbstractIterator
import kotlinx.io.files.Path

public open class SFtpClient(
    host: FtpHost,
    config: FtpClientConfig,
) : AbstractFtpClient(host, config) {

    private val loggerFactory: LoggerFactory = LoggerFactory.DEFAULT

    private val client = SSHClient()
    private val sftp: SFTPClient = client.newSFTPClient()

    override var implicit: Boolean = false
    override var utf8: Boolean = false
    override var passive: Boolean = false

    override val isConnected: Boolean
        get() = client.isConnected && client.isAuthenticated

    override var privateData: Boolean = false

    override fun connect() {
        with(config) {
            hostKeyVerifierFingerprint?.let { client.addHostKeyVerifier(it) }
            with(host) {
                client.connect(host, port)
            }
        }
    }

    override fun login() {
        with(config) {
            with(host) {
                if (privateKey == null) {
                    client.authPassword(username, password)
                }
                else {
                    val kp = client.loadKeys(privateKey, password)
                    client.authPublickey(username, kp)
                }
            }
        }
    }

    override fun metadata(path: Path): PathMetadata = sftp.lstat(path.toString()).toPathMetadata(path)

    override fun list(path: Path?): Collection<PathMetadata> =
        sftp.ls(path?.toString() ?: ".").map { remoteResourceInfo ->
            remoteResourceInfo.attributes.toPathMetadata(remoteResourceInfo.path.toPath())
        }

    public override fun createDirectory(path: Path): Boolean {
        sftp.mkdir(path.toString())
        return true
    }

    override fun createSymlink(source: Path, destination: Path): Boolean {
        sftp.symlink(source.toString(), destination.toString())
        return true
    }

    override fun move(source: Path, destination: Path): Boolean {
        sftp.rename(source.toString(), destination.toString())
        return true
    }

    override fun copyFile(source: Path, destination: Path, append: Boolean): Boolean {
        val copied: Boolean
        val remoteFile = sftp.open(source.toString())
        try {
            val remoteInputStream: ReadAheadRemoteFileInputStream = remoteFile.ReadAheadRemoteFileInputStream(16)
            try {
                copied = copy(remoteInputStream, destination, append)
            }
            finally {
                remoteInputStream.close()
            }
        }
        finally {
            remoteFile.close()
        }
        return copied
    }

    override fun delete(path: Path): Boolean {
        var deleted = false
        try {
            sftp.rm(path.toString())
            deleted = true
        }
        catch (_: Throwable) {
        }
        try {
            sftp.rmdir(path.toString())
            deleted = true
        }
        catch (_: Throwable) {
        }
        return deleted
    }

    override fun readFile(
        path: Path,
        bufferSize: Int,
    ): AbstractClosableAbstractIterator<ByteArray> {
        val rf = sftp.open(path.toString())
        try {
            return rf.ReadAheadRemoteFileInputStream(16).iterator(rf::close, bufferSize)
        }
        catch (e: Throwable) {
            rf.close()
            throw e
        }
    }

    override fun writeFile(
        path: Path,
        data: Iterator<ByteArray>,
        append: Boolean,
    ): Boolean = copy(data.asSequence().flatMap(ByteArray::asSequence).iterator().asInputStream(), path, append)

    override fun close() {
        sftp.close()
        client.disconnect()
    }

    private fun copy(
        fromInputStream: InputStream,
        destination: Path,
        append: Boolean = false,
    ): Boolean {
        var rf: RemoteFile? = null

        var rfos: RemoteFileOutputStream? = null

        try {
            rf = sftp.open(destination.toString(), toFileModes(append))

            rfos = rf.RemoteFileOutputStream(rf.fetchAttributes().size, 16)

            StreamCopier(fromInputStream, rfos, loggerFactory)
                .bufSize(
                    sftp.sftpEngine.subsystem.remoteMaxPacketSize - rf.outgoingPacketOverhead,
                ).keepFlushing(false)
                .copy()
        }
        catch (_: Throwable) {
            return false
        }
        finally {
            if (rf != null) {
                try {
                    rf.close()
                }
                catch (_: IOException) {
                }
            }
            if (rfos != null) {
                try {
                    rfos.close()
                }
                catch (_: IOException) {
                }
            }
        }
        return true
    }
}

internal fun toFileModes(append: Boolean): EnumSet<OpenMode> =
    if (append) EnumSet.of(OpenMode.WRITE, OpenMode.APPEND)
    else EnumSet.of(OpenMode.WRITE, OpenMode.CREAT, OpenMode.TRUNC)

internal fun FileAttributes.toPathMetadata(path: Path): PathMetadata =
    PathMetadata(
        path,
        type == FileMode.Type.REGULAR,
        type == FileMode.Type.DIRECTORY,
        if (type == FileMode.Type.SYMLINK) Path("") else null,
        size,
        lastModifiedAtMillis = mtime * 1000L,
        lastAccessedAtMillis = atime * 1000L,
    )
