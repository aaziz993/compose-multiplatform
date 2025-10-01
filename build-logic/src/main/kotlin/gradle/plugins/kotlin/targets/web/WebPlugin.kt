package gradle.plugins.kotlin.targets.web

import gradle.api.kotlin.mpp.kotlin
import gradle.api.project.ProjectLayout
import gradle.api.project.projectScript
import klib.data.type.primitives.string.uppercaseFirstChar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

public class WebPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustStatic()
            }
        }
    }

    private fun Project.adjustStatic() = kotlin.targets.withType<KotlinJsTargetDsl> {
        browser {
            commonWebpackConfig {
                devServer?.static?.addAll(
                    when (val layout = project.projectScript.layout) {
                        ProjectLayout.Default ->
                            compilations.map { compilation ->
                                "src/${targetName}${compilation.name.uppercaseFirstChar()}/kotlin"
                            }

                        is ProjectLayout.Flat -> compilations.map { compilation ->
                            "${
                                if (compilation.name == KotlinCompilation.MAIN_COMPILATION_NAME) "src"
                                else compilation.name
                            }${layout.targetDelimiter}$targetName"
                        }
                    },
                )
            }
        }
    }
}
