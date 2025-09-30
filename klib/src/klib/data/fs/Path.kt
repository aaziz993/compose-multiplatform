package klib.data.fs

import kotlinx.io.files.Path

public fun pathOrNull(base: String, vararg parts: String): Path? =
    try {
        Path(base, *parts)
    }
    catch (_: IllegalArgumentException) {
        null
    }
