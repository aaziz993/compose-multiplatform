package klib.data.model

import klib.data.type.collections.bimap.biMapOf

internal val MIME_TYPES = biMapOf(
    MimeType.PDF to "application/pdf",
    MimeType.TEXT to "text/plain",
    MimeType.IMAGE to "image/*",
)
