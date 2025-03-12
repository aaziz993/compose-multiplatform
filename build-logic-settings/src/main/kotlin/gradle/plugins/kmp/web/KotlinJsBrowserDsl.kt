package gradle.plugins.kmp.web

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
    fun applyTo(browser: KotlinJsBrowserDsl, outputName: String) {
        super.applyTo(browser, outputName)
        commonWebpackConfig?.let { commonWebpackConfig ->
            browser.commonWebpackConfig {
                commonWebpackConfig.applyTo(this, "$outputName.js")
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
