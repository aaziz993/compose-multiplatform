package data.location.locale

import androidx.compose.runtime.Composable
import clib.data.location.locale.asStringResource
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.location.locale.Locale

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Locale.asStringResource(): String = asStringResource(Res.allStringResources)
