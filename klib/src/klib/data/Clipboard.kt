package klib.data

public expect suspend fun String.toClipboard()

public expect suspend fun fromClipboard(): String?
