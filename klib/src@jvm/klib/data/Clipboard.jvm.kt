package klib.data

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val CLIPBOARD: Clipboard
    get() = Toolkit.getDefaultToolkit().systemClipboard

public actual suspend fun String.toClipboard(): Unit =
    withContext(Dispatchers.IO) {
        CLIPBOARD.setContents(StringSelection(this@toClipboard), null)
    }

public actual suspend fun fromClipboard(): String? =
    withContext(Dispatchers.IO) {
        try {
            if (CLIPBOARD.isDataFlavorAvailable(DataFlavor.stringFlavor)) CLIPBOARD.getData(DataFlavor.stringFlavor) as String
            else null
        }
        catch (_: Exception) {
            null
        }
    }
