package clib.data

import androidx.compose.ui.platform.ClipEntry

public expect suspend fun ClipEntry.getText(): String?

public expect fun String.toClipEntry(): ClipEntry
