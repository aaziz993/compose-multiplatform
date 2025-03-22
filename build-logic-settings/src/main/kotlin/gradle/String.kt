package gradle

internal fun String.doubleQuoted() = "\"$this\""

internal fun String.decapitalized() = replaceFirstChar(Char::lowercase)

internal fun String.ifNotEmpty(transform: (String) -> String) =
    if (isNotEmpty()) transform(this) else this

internal fun String.addPrefix(prefix: String) =
    "$prefix$this"

internal fun String.addSuffix(suffix: String) =
    "$this$suffix"

internal fun String.prefixIfNotEmpty(prefix: String) =
    ifNotEmpty { "$prefix$it" }

internal fun String.suffixIfNotEmpty(suffix: String) =
    ifNotEmpty { "$it$suffix" }

private val URL_REGEX = "^(https|http)://.*".toRegex()

internal val String.isUrl
    get() = matches(URL_REGEX)

private val GITHUB_URL_REGEX = """https?://(www\.)?github\.com/.*""".toRegex()

internal val String.isGithubUrl
    get() = matches(GITHUB_URL_REGEX)
