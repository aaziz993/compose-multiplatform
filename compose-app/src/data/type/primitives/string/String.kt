package data.type.primitives.string

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.asStringResource
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources

@Suppress("ComposeUnstableReceiver")
@Composable
public fun String.asStringResource(defaultValue: () -> String = { this }): String =
    asStringResource(resources = Res.allStringResources, defaultValue = defaultValue)

@Suppress("ComposeUnstableReceiver")
@Composable
public fun <T> T.asStringResource(): String = asStringResource(Res.allStringResources)

