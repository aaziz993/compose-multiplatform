package klib.data.fs

import platform.Foundation.NSURL

public actual fun String.isValidFileUrl(): Boolean = try {
    val url = NSURL.URLWithString(this)
    url != null && url.scheme == "file" && url.path != null
}
catch (e: Exception) {
    false
}
