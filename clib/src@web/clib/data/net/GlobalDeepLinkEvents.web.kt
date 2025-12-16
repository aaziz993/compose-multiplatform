package clib.data.net

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.ktor.http.Url
import klib.data.net.addDeepLinkHandler
import klib.data.net.removeDeepLinkHandler
import klib.data.net.toUrl

@Composable
public actual fun GlobalDeepLinkEvents(
    vararg keys: Any,
    onEvent: (Url) -> Unit,
): Unit = DisposableEffect(*keys) {
    val (popStateEvent, hashChangeEvent) = addDeepLinkHandler { url -> onEvent(url.toUrl()) }
    onDispose { removeDeepLinkHandler(popStateEvent, hashChangeEvent) }
}
