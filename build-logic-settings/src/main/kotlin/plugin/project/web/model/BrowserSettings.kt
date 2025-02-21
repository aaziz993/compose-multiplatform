package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BrowserSettings(
    override val distribution: Distribution? = null,
    override val testTask: KotlinJsTest? = null,
    override val commonWebpackConfig: KotlinWebpackConfig? = null,
    override val runTask: KotlinWebpack? = null,
    override val webpackTask: KotlinWebpack? = null,
    val enabled: Boolean = true,
) : KotlinJsBrowserDsl
