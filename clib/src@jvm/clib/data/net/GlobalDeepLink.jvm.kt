package clib.data.net

import androidx.compose.runtime.Composable
import clib.data.type.collections.LaunchedEffect
import io.ktor.http.Url
import klib.data.net.GlobalDeepLinkController

@Composable
public actual fun GlobalDeepLink(
    vararg keys: Any,
    onEvent: (Url) -> Unit,
): Unit = GlobalDeepLinkController.events.LaunchedEffect(keys = keys, onEvent = onEvent)
