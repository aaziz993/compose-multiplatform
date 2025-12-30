package clib.presentation.events.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.ktor.http.Url
import klib.data.net.toUrl

@Composable
public actual fun GlobalDeepLink(
    vararg keys: Any,
    onEvent: (Url) -> Unit,
): Unit = DisposableEffect(*keys) {
    val (popStateEventUnsubscribe, hashChangeEventUnsubscribe) = addHandleDeeplinkEvents { url -> onEvent(url.toUrl()) }
    onDispose {
        popStateEventUnsubscribe()
        hashChangeEventUnsubscribe()
    }
}
