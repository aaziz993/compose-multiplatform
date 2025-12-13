package clib.data.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import klib.data.share.Share

@Composable
public actual fun rememberShare(): Share {
    val context = LocalContext.current
    return remember { Share(context) }
}
