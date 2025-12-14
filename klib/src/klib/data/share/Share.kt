package klib.data.share

import klib.data.share.model.ShareFile

public expect class Share {

    public suspend fun shareText(text: String)
    public suspend fun shareUrl(url: String)
    public suspend fun shareFile(file: ShareFile): Result<Unit>
}
