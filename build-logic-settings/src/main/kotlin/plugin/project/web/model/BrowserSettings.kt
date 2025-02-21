package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BrowserSettings(
   override val webpackTask: KotlinWebpack? = null,
   override   val commonWebpackConfig: KotlinWebpackConfig? = null,
    val enabled: Boolean = true
): KotlinJsBrowserDsl
