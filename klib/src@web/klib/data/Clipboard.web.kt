package klib.data

import web.clipboard.readText
import web.clipboard.writeText
import web.navigator.navigator

public actual suspend fun String.toClipboard(): Unit = navigator.clipboard.writeText(this)

public actual suspend fun fromClipboard(): String? = navigator.clipboard.readText()
