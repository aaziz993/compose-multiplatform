package clib.data

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun ClipEntry.getText(): String? = withContext(Dispatchers.IO) {
    runCatching {
        val itemCount = clipData.itemCount
        var textFull = ""
        for (i in 0 until itemCount) {
            val item = clipData.getItemAt(i)
            val text = item?.text
            if (text != null)
                textFull += text
        }
        textFull.ifEmpty { null }
    }.getOrNull()
}

public actual fun String.toClipEntry(): ClipEntry = ClipEntry(ClipData.newPlainText("label", this))
