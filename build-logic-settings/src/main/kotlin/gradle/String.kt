package gradle

internal fun String.decapitalized() = replaceFirstChar(Char::lowercase)

internal fun String.ifNotEmpty(transform: (String) -> String) =
    if (isNotEmpty()) transform(this) else this

internal fun String.prefixIfNotEmpty(prefix: String) =
    ifNotEmpty { "$prefix$it" }

private val URL_REGEX = "^(https|http)://.*".toRegex()

internal val String.isUrl
    get() = matches(URL_REGEX)
