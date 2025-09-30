package klib.data.net.http

import io.ktor.http.*

public val Regex.Companion.HTTP_PATTERN: String
    get() = "^https?://.*"

public val Regex.Companion.HTTP
    get() = HTTP_PATTERN.toRegex()

public val Regex.Companion.GITHUB_HTTP_PATTERN: String
    get() = """https?://(www\.)?github\.com/.*"""

public val Regex.Companion.GITHUB_HTTP
    get() = GITHUB_HTTP_PATTERN.toRegex()

public fun String.isGithubUrl(): Boolean = matches(Regex.GITHUB_HTTP)

public fun Url.Companion.parseOrNull(value: String): Url? = try {
    Url(value)
}
catch (_: URLParserException) {
    null
}

public fun Url.Companion.parse(value: String): Url =
    parseOrNull(value) ?: throw IllegalArgumentException("Invalid URL: $this")

public fun String.toHttpUrlOrNull(): Url? = Url.parseOrNull(this)

public fun String.toHttpUrl(): Url = Url.parse(this)

public fun String.isHttpUrl(): Boolean = matches(Regex.HTTP)

public fun String.isValidHttpUrl(): Boolean =
    toHttpUrlOrNull()?.let { it.protocol == URLProtocol.HTTP || it.protocol == URLProtocol.HTTPS } == true

public fun String.encodeHttpUrl(): String = URLBuilder().apply { encodedPath = this@encodeHttpUrl }.buildString()

public fun String.decodeHttpUrl(): String = URLBuilder().apply { path(this@decodeHttpUrl) }.buildString()

