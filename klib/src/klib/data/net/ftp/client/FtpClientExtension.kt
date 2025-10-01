package klib.data.net.ftp.client

import klib.data.net.ftp.client.model.FtpClientConfig
import klib.data.net.ftp.client.model.FtpHost

public expect fun createFtpClient(
    host: FtpHost,
    block: FtpClientConfig.() -> Unit = {},
): AbstractFtpClient
