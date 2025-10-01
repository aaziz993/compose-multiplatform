package klib.data.net.ftp.client

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPCmd
import org.apache.commons.net.ftp.FTPFile
import org.apache.commons.net.ftp.FTPReply
import java.io.IOException
import klib.data.fs.path.PathMetadata
import klib.data.fs.path.toPath
import klib.data.net.ftp.client.model.FtpClientConfig
import klib.data.net.ftp.client.model.FtpHost
import klib.data.type.collections.asInputStream
import klib.data.type.collections.iterator
import klib.data.type.collections.iterator.AbstractClosableAbstractIterator
import kotlinx.io.files.Path

public class FtpClientImpl(
    private val client: FTPClient,
    host: FtpHost,
    config: FtpClientConfig,
) : AbstractFtpClient(host, config) {

    override var implicit: Boolean = false

    override var utf8: Boolean = false
        set(value) {
            if (value) client.controlEncoding = "UTF-8"
            field = value
        }

    override var passive: Boolean = false
        set(value) {
            if (value) {
                client.enterLocalPassiveMode()
            }
            else {
                client.enterLocalActiveMode()
            }
            field = value
        }

    override val isConnected: Boolean
        get() = client.isConnected

    override var privateData: Boolean = false

    private var supportsMlsCommands = false

    override fun connect() {
        with(host) {
            client.connect(host, port)
        }

        if (!FTPReply.isPositiveCompletion(client.replyCode)) {
            client.disconnect()
            throw IOException("Exception in connecting to FTP Server")
        }
    }

    override fun login() {
        with(host) {
            client.login(username, password)
        }
        client.setFileType(FTP.BINARY_FILE_TYPE)
        supportsMlsCommands = client.hasFeature(FTPCmd.MLST)
    }

    override fun metadata(path: Path): PathMetadata =
        if (supportsMlsCommands) client.mlistFile(path.toString()).destinationMetadata()
        else throw IllegalStateException("Ftp server does not support MLST command.")

    override fun list(path: Path?): Collection<PathMetadata> =
        if (path == null) {
            if (supportsMlsCommands) client.mlistDir() else client.listFiles()
        }
        else {
            if (supportsMlsCommands) client.mlistDir(path.toString()) else client.listFiles(path.toString())
        }.map(FTPFile::destinationMetadata)

    override fun createDirectory(path: Path): Boolean = client.makeDirectory(path.toString())

    override fun createSymlink(
        source: Path,
        destination: Path,
    ): Boolean =
        // FTP does not support symbolic links, so this method may not be applicable.
        throw UnsupportedOperationException("FTP does not support symbolic links.")

    override fun move(
        source: Path,
        destination: Path,
    ): Boolean = client.rename(source.toString(), destination.toString())

    override fun copyFile(
        source: Path,
        destination: Path,
        append: Boolean
    ): Boolean = client.storeFile(destination.toString(), client.retrieveFileStream(source.toString()))

    override fun delete(path: Path): Boolean {
        try {
            if (client.deleteFile(path.toString())) return true
        }
        catch (_: Throwable) {
        }
        try {
            if (client.removeDirectory(path.toString())) return true
        }
        catch (_: Throwable) {
        }
        return false
    }

    override fun readFile(
        path: Path,
        bufferSize: Int,
    ): AbstractClosableAbstractIterator<ByteArray> = client.retrieveFileStream(path.toString()).iterator(
        {
            client.completePendingCommand()
        },
        bufferSize,
    )

    override fun writeFile(
        path: Path,
        data: Iterator<ByteArray>,
        append: Boolean,
    ): Boolean =
        if (append) {
            client.appendFile(path.toString(), data.asSequence().flatMap(ByteArray::asSequence).iterator().asInputStream())
        }
        else {
            client.storeFile(path.toString(), data.asSequence().flatMap(ByteArray::asSequence).iterator().asInputStream())
        }.also {
            client.completePendingCommand()
        }

    override fun close() {
        client.logout()
        client.disconnect()
    }
}

private fun FTPFile.destinationMetadata(): PathMetadata =
    PathMetadata(
        name.toPath(),
        isFile,
        isDirectory,
        if (isSymbolicLink) link.toPath() else null,
        size,
        lastModifiedAtMillis = timestampInstant.toEpochMilli(),
    )
