package klib.data.net.ftp.client.model

public val Regex.Companion.FTP_PATTERN: String
    get() = "^(ftp|ftps|sftp)://.*"

public val Regex.Companion.FTP: Regex
    get() = FTP_PATTERN.toRegex()

public val Regex.Companion.FTP_URL_PATTERN: String
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
                Regex.FTP_URL.matchEntire(url)
                    ?: throw IllegalArgumentException("Invalid FTP url: $url")

            val (scheme, username, password, host, port, path) = matchResult.destructured
            val ftpScheme = FtpScheme.valueOf(scheme.uppercase())
            return FtpUrl(
                 ftpScheme,
                 username.ifBlank { ftpScheme.defaultUsername },
                if (username == ftpScheme.defaultUsername) "" else password.ifBlank { "" },
                 host,
                 if (port.isBlank()) ftpScheme.defaultPort else port.toInt(),
                path.ifBlank { "/" },
            )
        }
    }
}

public fun String.isFtpUrl(): Boolean = matches(Regex.FTP)

public fun String.isValidFtpUrl(): Boolean = matches(Regex.FTP_URL)
