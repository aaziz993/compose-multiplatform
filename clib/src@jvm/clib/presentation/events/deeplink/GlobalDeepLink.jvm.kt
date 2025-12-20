package clib.presentation.events.deeplink

import androidx.compose.runtime.Composable
import clib.data.type.collections.LaunchedEffect
import io.ktor.http.Url

@Composable
public actual fun GlobalDeepLink(
    vararg keys: Any,
    onEvent: (Url) -> Unit,
): Unit = GlobalDeeplinkEventController.events.LaunchedEffect(keys = keys, onEvent = onEvent)
