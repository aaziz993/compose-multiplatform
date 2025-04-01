package klib.data.type

import java.net.URI
import java.nio.file.InvalidPathException
import java.nio.file.Paths

internal fun String.doubleQuoted() = "\"$this\""

internal fun String.decapitalized() = replaceFirstChar(Char::lowercase)

internal fun String.ifNotEmpty(transform: (String) -> String) =
    if (isNotEmpty()) transform(this) else this

internal fun String.addPrefix(prefix: String) =
    "$prefix$this"

internal fun String.addSuffix(suffix: String) =
    "$this$suffix"

internal fun String.addPrefixIfNotEmpty(prefix: String) =
    ifNotEmpty { "$prefix$it" }

internal fun String.addSuffixIfNotEmpty(suffix: String) =
    ifNotEmpty { "$it$suffix" }

internal val String.isValidUrl
    get() =
        try {
            URI(this).let { uri ->
                uri.scheme.equals("http", true) || uri.scheme.equals("https", true)
            }
        }
        catch (_: Exception) {
            false
        }

internal val String.isPath
    get() = try {
        Paths.get(this)
        true
    }
    catch (e: Exception) {
        when (e) {
            is InvalidPathException, is NullPointerException -> false
            else -> throw e
        }
    }

private val GITHUB_URL_REGEX = """https?://(www\.)?github\.com/.*""".toRegex()

internal val String.isGithubUrl
    get() = matches(GITHUB_URL_REGEX)
