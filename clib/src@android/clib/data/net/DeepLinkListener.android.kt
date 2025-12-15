package clib.data.net

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import io.ktor.http.Url
import klib.data.net.toUrl

@Composable
public actual fun DeepLinkListener(
    vararg keys: Any,
    onEvent: (Url) -> Unit,
) {
    val activity = LocalActivity.current as? ComponentActivity ?: return

    activity.intent.fireDeepLink(onEvent)

    DisposableEffect(*keys) {
        val listener = Consumer<Intent> { intent -> intent.fireDeepLink(onEvent) }
        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
}

private fun Intent.fireDeepLink(listener: (Url) -> Unit) {
    dataString?.toUrl()?.let(listener)
}

