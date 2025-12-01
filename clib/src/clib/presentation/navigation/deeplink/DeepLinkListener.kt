package clib.presentation.navigation.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import clib.presentation.navigation.deeplink.DeepLinkState.consumeDeepLink
import clib.presentation.navigation.deeplink.DeepLinkState.deepLinkFlow
import clib.presentation.navigation.nav3Logger
import io.ktor.http.Url

@Composable
public fun DeepLinkListener(listener: (Url) -> Unit) {
    val deepLinkState by deepLinkFlow.collectAsStateWithLifecycle()
    LaunchedEffect(deepLinkState) {
        consumeDeepLink()?.let { url ->
            nav3Logger.debug { "Handled deep link '$url'" }
            listener(url)
        }
    }
}
