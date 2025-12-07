package clib.data.location.locale

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.asStringResource
import klib.data.location.locale.Locale
import org.jetbrains.compose.resources.StringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Locale.asStringResource(resources: Map<String, StringResource>): String =
    "locale_${toString().replace('-', '_').lowercase()}"
        .asStringResource(resources) { toString() }
