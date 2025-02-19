package plugin.project.web.js

import gradle.kotlin
import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.BindingPluginPart
import plugin.project.model.hasJs
import plugin.project.model.js
import plugin.project.web.configureJsTestTasks
import plugin.project.web.configureKotlinJsTarget
import plugin.project.web.js.karakum.configureKarakum

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class JsBindingPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply by lazy { project.moduleProperties.targets.hasJs }

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        with(project) {
            moduleProperties.targets.js.forEach { target -> target.applyTo(project.kotlin) }

            applySettings()
        }
    }

    override fun applyAfterEvaluate() = with(project) {
        configureKarakum()
    }

    private fun applySettings() = with(project) {
        configureKotlinJsTarget<KotlinJsTargetDsl>()
        configureJsTestTasks<KotlinJsTargetDsl>()
    }
}

