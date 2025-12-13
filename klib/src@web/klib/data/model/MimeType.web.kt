package klib.data.model

import klib.data.type.collections.bimap.biMapOf

internal val MIME_TYPES = biMapOf(
    MimeType.TEXT to "text/plain",
    MimeType.PDF to "application/pdf",
    MimeType.IMAGE to "image/png",
)
