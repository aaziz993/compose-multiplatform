package plugin.project.web.model

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBrowserDsl

internal interface KotlinJsBrowserDsl : KotlinJsSubTargetDsl {

    val commonWebpackConfig: KotlinWebpackConfig?

    val runTask: KotlinWebpack?

    val webpackTask: KotlinWebpack?

    context(Project)
    fun applyTo(browser: KotlinJsBrowserDsl) {
        super.applyTo(browser)
        commonWebpackConfig?.let { commonWebpackConfig ->
            browser.commonWebpackConfig {
                commonWebpackConfig.applyTo(this)
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
