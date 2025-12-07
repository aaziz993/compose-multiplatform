package clib.data.location.currency

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.asStringResource
import klib.data.location.currency.Currency
import org.jetbrains.compose.resources.StringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Currency.asStringResource(resources: Map<String, StringResource>): String =
    "currency_${toString().replace('-', '_').lowercase()}"
        .asStringResource(resources) { toString() }
