package data.location.currency

import androidx.compose.runtime.Composable
import clib.data.location.currency.asStringResource
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.location.currency.Currency

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Currency.asStringResource(): String = asStringResource(Res.allStringResources)
