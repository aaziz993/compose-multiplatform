package klib.data.type

import java.net.URI

public val String.asURIOrNull: URI?
    get() = try {
        URI(this)
    }
    catch (_: Exception) {
        null
    }

public val String.isValidHttpUrl: Boolean
    get() = asURIOrNull?.let { uri ->
        uri.scheme.equals("http", true) || uri.scheme.equals("https", true)
    } == true

private val GITHUB_URL_REGEX = """https?://(www\.)?github\.com/.*""".toRegex()

public val String.isGithubUrl: Boolean
    get() = matches(GITHUB_URL_REGEX)
