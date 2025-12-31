package klib.data.network.ftp.client

import klib.data.network.ftp.client.model.FtpClientConfig
import klib.data.network.ftp.client.model.FtpHost

public expect fun createFtpClient(
    host: FtpHost,
    block: FtpClientConfig.() -> Unit = {},
): AbstractFtpClient
