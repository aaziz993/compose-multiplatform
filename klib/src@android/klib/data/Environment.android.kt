package klib.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import splitties.init.appCtx

private val CLIPBOARD_MANAGER: ClipboardManager
    get() = appCtx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

public actual suspend fun String.toClipboard(): Unit =
    withContext(Dispatchers.IO) {
        CLIPBOARD_MANAGER.setPrimaryClip(ClipData.newPlainText("text", this@toClipboard))
    }

public actual suspend fun fromClipboard(): String? =
    withContext(Dispatchers.IO) {
        CLIPBOARD_MANAGER.primaryClip?.let {
            if (it.itemCount > 0) it.getItemAt(0).text.toString() else null
        }
    }
