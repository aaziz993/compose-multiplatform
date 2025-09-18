package klib.data.type

import io.ktor.http.*

public val HTTP_REGEX: Regex = "^https?://.*".toRegex(RegexOption.IGNORE_CASE)

public val GITHUB_URL_REGEX: Regex = """https?://(www\.)?github\.com/.*""".toRegex()

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

public fun String.isHttpUrl(): Boolean = matches(HTTP_REGEX)

public fun String.isValidHttpUrl(): Boolean =
    toHttpUrlOrNull()?.let { it.protocol == URLProtocol.HTTP || it.protocol == URLProtocol.HTTPS } == true

public fun String.encodeHttpUrl(): String = URLBuilder().apply { encodedPath = this@encodeHttpUrl }.buildString()

public fun String.decodeHttpUrl(): String = URLBuilder().apply { path(this@decodeHttpUrl) }.buildString()

public fun String.isGithubUrl(): Boolean = matches(GITHUB_URL_REGEX)

