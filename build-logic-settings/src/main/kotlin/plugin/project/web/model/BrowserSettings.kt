package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BrowserSettings(
    val webpackTask: KotlinWebpack? = null,
    val commonWebpackConfig: KotlinWebpackConfig? = null,
    val enabled: Boolean = true
)
