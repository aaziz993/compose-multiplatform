package clib.data

import androidx.compose.ui.platform.ClipEntry
import kotlinx.coroutines.await
import org.w3c.files.Blob
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public actual suspend fun ClipEntry.getText(): String? =
    runCatching {
        val item = clipboardItems[0] ?: return null

        // Check if the item supports 'text/plain'.
        val hasTextPlain = item.types.toList().map {
            it.toString()
        }.any { it == "text/plain" }
        if (!hasTextPlain) return null

        // Read the Blob as text.
        val blob = item.getType("text/plain".toJsString()).await<Blob>()
        blob.readAsText()
    }.getOrNull()

// Helper to read text from a Blob.
private suspend fun Blob.readAsText() = suspendCoroutine { cont ->
    val reader = FileReader()
    reader.onload = {
        cont.resume(reader.result.toString())
    }
    reader.onerror = {
        cont.resume(null)
    }
    reader.readAsText(this)
}

public actual fun String.toClipEntry(): ClipEntry = ClipEntry.withPlainText(this)

