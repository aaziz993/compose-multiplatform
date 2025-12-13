package klib.data.share

import klib.data.share.model.ShareFileModel

public expect class Share {

    public suspend fun shareText(text: String)
    public suspend fun shareFile(file: ShareFileModel): Result<Unit>
}
