package gradle.plugins.kotlin.targets.web

import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

public class WasmJsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustStatic()
            }
        }
    }

    private fun Project.adjustStatic() = when (val layout = project.projectScript.layout) {
        is ProjectLayout.Flat -> kotlin.targets.withType<KotlinWasmJsTargetDsl> {
            browser {
                commonWebpackConfig {
                    devServer?.static?.add("src${layout.targetDelimiter}$name")
                }
            }
        }

        else -> Unit
    }
}
