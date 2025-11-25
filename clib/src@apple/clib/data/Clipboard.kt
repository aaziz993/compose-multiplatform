package clib.data

import androidx.compose.ui.platform.ClipEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

public actual suspend fun ClipEntry.getText() = withContext(Dispatchers.IO) {
    getPlainText()
}

public actual fun String.toClipEntry(): ClipEntry = ClipEntry.withPlainText(this)
