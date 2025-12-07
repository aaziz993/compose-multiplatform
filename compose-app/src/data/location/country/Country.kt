package data.location.country

import androidx.compose.runtime.Composable
import clib.data.location.country.asStringResource
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.location.country.Country

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Country.asStringResource(): String = asStringResource(Res.allStringResources)

