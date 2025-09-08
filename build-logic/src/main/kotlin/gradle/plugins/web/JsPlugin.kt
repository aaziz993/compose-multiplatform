package gradle.plugins.web

import gradle.api.configureEach
import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

public class JsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustStatic()
            }
        }
    }

    private fun Project.adjustStatic() = when (val layout = project.projectProperties.layout) {
        is ProjectLayout.Flat -> kotlin.targets.withType<KotlinJsTargetDsl>()
            .matching { target -> target::class == KotlinJsTargetDsl::class }
            .configureEach { target ->
                target.browser {
                    commonWebpackConfig {
                        devServer?.static?.add("src${layout.targetDelimiter}${target.name}")
                    }
                }
            }

        else -> Unit
    }
}
