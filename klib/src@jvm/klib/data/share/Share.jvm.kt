package klib.data.share

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import klib.data.share.model.ShareFile

public actual class Share {

    public actual suspend fun shareText(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), null)
    }

    public actual suspend fun shareUrl(url: String): Unit = shareText(url)

    public actual suspend fun shareFile(file: ShareFile): Result<Unit> = runCatching {
        val tempFile = File.createTempFile(
            file.fileName.substringBeforeLast('.'),
            ".${file.fileName.substringAfterLast('.')}",
        )

        tempFile.writeBytes(file.bytes)
        tempFile.deleteOnExit()

        Desktop.getDesktop().open(tempFile)
    }
}
