package klib.data.type

import java.net.URI

private val GITHUB_URL_REGEX = """https?://(www\.)?github\.com/.*""".toRegex()

public fun String.toURIOrNull(): URI? = try {
    URI(this)
} catch (_: Exception) {
    null
}

public fun String.isValidHttpUrl(): Boolean = toURIOrNull()?.let { uri ->
    uri.scheme.equals("http", true) || uri.scheme.equals("https", true)
} == true

public fun String.isGithubUrl(): Boolean = matches(GITHUB_URL_REGEX)
