package klib.data.share

import js.array.jsArrayOf
import js.objects.unsafeJso
import js.typedarrays.toUint8Array
import klib.data.model.MIME_TYPES
import klib.data.share.model.ShareFile
import kotlin.js.ExperimentalWasmJsInterop
import web.blob.Blob
import web.clipboard.writeText
import web.file.File
import web.navigator.navigator
import web.share.ShareData

public actual class Share {

    public actual suspend fun shareText(text: String) {
        val data = unsafeJso<ShareData> {
            this.text = text
        }

        if (navigator.canShare(data)) navigator.shareAsync(data) else navigator.clipboard.writeText(text)
    }

    public actual suspend fun shareUrl(url: String) {
        val data = unsafeJso<ShareData> {
            this.url = url
        }

        if (navigator.canShare(data)) navigator.shareAsync(data) else navigator.clipboard.writeText(url)
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    public actual suspend fun shareFile(file: ShareFile): Result<Unit> = runCatching {
        val blob = Blob(
            jsArrayOf(file.bytes.toUint8Array()),
            unsafeJso {
                type = MIME_TYPES[file.mime]
            },
        )

        val jsFile = File(
            jsArrayOf(blob),
            file.fileName,
            unsafeJso {
                type = blob.type
            },
        )

        val data = unsafeJso<ShareData> {
            files = jsArrayOf(jsFile)
        }

        if (!navigator.canShare(data)) {
            error("File sharing not supported on this browser")
        }

        navigator.shareAsync(data)
    }
}
