package clib.data.net

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.ktor.http.Url
import klib.data.net.DeepLinkState.consumeDeepLink
import klib.data.net.DeepLinkState.deepLinkFlow

@Composable
public actual fun DeepLinkListener(listener: (Url) -> Unit) {
    val deepLinkState by deepLinkFlow.collectAsStateWithLifecycle()
    LaunchedEffect(deepLinkState) {
        consumeDeepLink()?.let(listener)
    }
}
