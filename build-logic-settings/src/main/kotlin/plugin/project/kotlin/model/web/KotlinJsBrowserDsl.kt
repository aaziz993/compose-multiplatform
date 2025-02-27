package plugin.project.kotlin.model.web

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBrowserDsl

@Serializable
internal data class KotlinJsBrowserDsl(
    override val distribution: Distribution? = null,
    override val testTask: KotlinJsTest? = null,
    val commonWebpackConfig: KotlinWebpackConfig? = null,
    val runTask: KotlinWebpack? = null,
    val webpackTask: KotlinWebpack? = null,
) : KotlinJsSubTargetDsl {

    context(Project)
    fun applyTo(browser: KotlinJsBrowserDsl, defaultModuleName: String? = null) {
        super.applyTo(browser)
        commonWebpackConfig?.let { commonWebpackConfig ->
            browser.commonWebpackConfig {
                commonWebpackConfig.applyTo(this, defaultModuleName)
            }
        }

        runTask?.let { runTask ->
            browser.runTask {
                runTask.applyTo(this)
            }
        }

        webpackTask?.let { webpackTask ->
            browser.webpackTask {
                webpackTask.applyTo(this)
            }
        }
    }
}
