package klib.data.fs

import klib.data.BUFFER_SIZE
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path

public val Regex.Companion.FILE_PATTERN: String
    get() = "^file://.*"

public val Regex.Companion.FILE: Regex
    get() = FILE_PATTERN.toRegex()

public fun String.isFileUrl(): Boolean = matches(Regex.FILE)

public expect fun String.isValidFileUrl(): Boolean

public expect fun FileSystem.canonicalize(path: Path): Path

public expect fun FileSystem.createSymlink(source: Path, destination: Path)

@Throws(IOException::class)
public fun FileSystem.copy(source: Path, destination: Path, bufferSize: Long = BUFFER_SIZE.toLong()) {
    source(source).use { bytesIn ->
        sink(destination).use { bytesOut ->
            val buffer = Buffer()
            while (true) {
                val bytesRead = bytesIn.readAtMostTo(buffer, bufferSize)
                if (bytesRead <= 0) break
                bytesOut.write(buffer, bytesRead)
            }
        }
    }
}
