package klib.data.network.ftp.client

import klib.data.network.ftp.client.model.FtpClientConfig
import klib.data.network.ftp.client.model.FtpHost

public actual fun createFtpClient(
    host: FtpHost,
    block: FtpClientConfig.() -> Unit,
): AbstractFtpClient = throw NotImplementedError()
