package gradle

private val URL_REGEX = "^(https|http)://".toRegex()

internal val String.isUrl
    get() = matches(URL_REGEX)
