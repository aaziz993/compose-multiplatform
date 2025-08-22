package klib.data.type

import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths

public fun pathOrNull(first: String, vararg more: String): Path? = try {
    Paths.get(first, *more)
} catch (_: InvalidPathException) {
    null
} catch (_: NullPointerException) {
    null
}