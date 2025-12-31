package config

import klib.config.UIConfig
import kotlinx.serialization.Serializable

@Serializable
public data class UIConfig(
    override val startRoute: String? = null,
    override val authRedirectRoute: String? = null,
    override val authRoute: String? = null,
    override val routes: Map<String, RouteConfig> = emptyMap(),
    override val databaseName: String? = null,
) : UIConfig
