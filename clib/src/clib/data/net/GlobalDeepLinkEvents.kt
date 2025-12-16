package clib.data.net

import androidx.compose.runtime.Composable
import io.ktor.http.Url

@Composable
public expect fun GlobalDeepLinkEvents(
    vararg keys: Any,
    onEvent: (Url) -> Unit,
)
