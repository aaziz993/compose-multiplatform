package klib.data.type

import com.ashampoo.kim.Kim
import com.ashampoo.kim.format.ImageMetadata
import com.ashampoo.kim.model.MetadataUpdate

public fun ByteArray.readImageMetadata(): ImageMetadata? = Kim.readMetadata(this)

public fun ByteArray.updateImageMetadata(update: MetadataUpdate): ByteArray = Kim.update(this, update)

public fun ByteArray.updateImageThumbnail(thumbnailBytes: ByteArray): ByteArray = Kim.updateThumbnail(this, thumbnailBytes)
