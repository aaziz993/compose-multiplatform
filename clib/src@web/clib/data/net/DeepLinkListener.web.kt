package clib.data.net

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.ktor.http.Url
import klib.data.net.addDeepLinkHandler
import klib.data.net.removeDeepLinkHandler
import klib.data.net.toUrl

@Composable
public actual fun DeepLinkListener(listener: (Url) -> Unit) {
    DisposableEffect(Unit) {
        val (popStateEvent, hasChangeEvent) = addDeepLinkHandler { url -> listener(url.toUrl()) }
        onDispose { removeDeepLinkHandler(popStateEvent, hasChangeEvent) }
    }
}
