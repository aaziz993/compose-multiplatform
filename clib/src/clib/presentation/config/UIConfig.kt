package clib.presentation.config

import androidx.compose.ui.unit.Density
import clib.presentation.appbar.model.AppBar
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.theme.density.model.DensitySerial
import clib.presentation.theme.model.Theme
import klib.data.config.UIConfig
import kotlinx.serialization.Serializable

@Serializable
public data class UIConfig(
    val appBar: AppBar = AppBar(),
    val connectivity: Connectivity = Connectivity(),
    val theme: Theme = Theme(),
    val density: DensitySerial = Density(2f),
    override val startRoute: String? = null,
    override val authRoute: String? = null,
    override val authRedirectRoute: String? = null,
    override val routes: Map<String, RouteConfig> = emptyMap(),
    override val databaseName: String? = null,
) : UIConfig
