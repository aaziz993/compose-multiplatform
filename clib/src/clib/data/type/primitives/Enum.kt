package clib.data.type.primitives

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.asStringResource
import org.jetbrains.compose.resources.StringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Enum<*>.asStringResource(resources: Map<String, StringResource>): String =
    name.asStringResource(resources) { name }
