package klib.data.share

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import klib.data.share.model.ShareFile
import kotlinx.coroutines.IO

public actual class Share {

    public actual suspend fun shareText(text: String) {
        val activityViewController = UIActivityViewController(listOf(text), null)
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            activityViewController, animated = true, completion = null,
        )
    }

    public actual suspend fun shareUrl(url: String): Unit = shareText(url)

    public actual suspend fun shareFile(file: ShareFile): Result<Unit> {
        return runCatching {
            val url = withContext(Dispatchers.IO) {
                saveFile(file.bytes, file.fileName)
            }
            val activityViewController = UIActivityViewController(listOf(url), null)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                activityViewController, animated = true, completion = null,
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun saveFile(bytes: ByteArray, name: String): NSURL? {
        val tempDir = NSTemporaryDirectory()
        val sharedFile = tempDir + name
        val saved = bytes.usePinned {
            val nsData = NSData.dataWithBytes(it.addressOf(0), bytes.size.toULong())
            nsData.writeToFile(sharedFile, true)
        }
        return if (saved) NSURL.fileURLWithPath(sharedFile) else null
    }
}
