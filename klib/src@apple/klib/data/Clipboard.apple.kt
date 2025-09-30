package klib.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.UIKit.UIPasteboard

public actual suspend fun String.toClipboard(): Unit =
    withContext(Dispatchers.IO) {
        UIPasteboard.generalPasteboard.string = this@toClipboard
    }

public actual suspend fun fromClipboard(): String? =
    withContext(Dispatchers.IO) {
        UIPasteboard.generalPasteboard.string
    }
