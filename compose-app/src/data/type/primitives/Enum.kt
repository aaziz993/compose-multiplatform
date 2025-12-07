package data.type.primitives

import androidx.compose.runtime.Composable
import clib.data.type.primitives.asStringResource
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Enum<*>.asStringResource(): String = asStringResource(Res.allStringResources)
