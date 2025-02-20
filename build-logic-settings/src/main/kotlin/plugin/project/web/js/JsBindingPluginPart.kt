package plugin.project.web.js

import gradle.kotlin
import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.BindingPluginPart
import plugin.project.model.TargetType
import plugin.project.model.add
import plugin.project.model.contains
import plugin.project.model.isDescendantOf
import plugin.project.web.configureJsTestTasks
import plugin.project.web.configureKotlinJsTarget
import plugin.project.web.js.karakum.configureKarakum

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class JsBindingPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply by lazy { TargetType.JS in project.moduleProperties.targets }

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        with(project) {
            moduleProperties.targets
                .filter { target -> target.type.isDescendantOf(TargetType.JS) }
                .forEach { target->target.add() }

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

