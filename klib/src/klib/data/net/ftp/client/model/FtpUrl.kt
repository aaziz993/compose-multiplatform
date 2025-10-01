package klib.data.net.ftp.client.model

public val Regex.Companion.FTP_PATTERN: Regex
    get() = "^(ftp|ftps|sftp)://.*"

public val Regex.Companion.FTP: Regex
    get() = FTP_PATTERN.toRegex()

public val Regex.Companion.FTP_URL_PATTERN: Regex
    get() = """^(ftp|ftps|sftp)://(?:(\w+)(?::(\w+))?@)?([^:/]+)(?::(\d+))?(/.*)?$"""
public val Regex.Companion.FTP_URL: Regex
    get() = FTP_URL_PATTERN.toRegex()

public class FtpUrl(
    scheme: FtpScheme,
    username: String = scheme.defaultUsername,
    password: String = "",
    host: String,
    port: Int = scheme.defaultPort,
    public val path: String = "/",
) : FtpHost(scheme, username, password, host, port) {

    override fun toString(): String = "${super.toString()}$path"

    public companion object {

        public operator fun invoke(url: String): FtpUrl {
            val matchResult =
                FTP_URL.matchEntire(url)
                    ?: throw IllegalArgumentException("Invalid FTP url: $url")

            val (scheme, username, password, host, port, path) = matchResult.destructured
            val ftpScheme = FtpScheme.valueOf(scheme.uppercase())
            return FtpUrl(
                scheme = ftpScheme,
                username = username.ifBlank { ftpScheme.defaultUsername },
                password = if (username == ftpScheme.defaultUsername) "" else password.ifBlank { "" },
                host = host,
                port = if (port.isBlank()) ftpScheme.defaultPort else port.toInt(),
                path = path.ifBlank { "/" },
            )
        }
    }
}

public fun String.isFtpUrl(): Boolean = matches(FTP)

public fun String.isValidFtpUrl(): Boolean = matches(FTP_URL)
