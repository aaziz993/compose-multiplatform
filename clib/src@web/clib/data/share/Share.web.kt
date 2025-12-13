package clib.data.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import klib.data.share.Share

@Composable
public actual fun rememberShare(): Share = remember { Share() }
