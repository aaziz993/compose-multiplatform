package clib.data

import androidx.compose.ui.platform.ClipEntry
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public actual suspend fun ClipEntry.getText(): String? = withContext(Dispatchers.IO) {
    runCatching {
        val transferable = nativeClipEntry as? Transferable
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
            transferable.getTransferData(DataFlavor.stringFlavor) as? String
        else null
    }.getOrNull()
}

public actual fun String.toClipEntry(): ClipEntry = ClipEntry(StringSelection(this))
