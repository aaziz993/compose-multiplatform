package plugin.project.web

import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import plugin.project.BindingPluginPart
import plugin.project.model.target.TargetType
import plugin.project.model.target.add
import plugin.project.model.target.contains
import plugin.project.model.target.isDescendantOf

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class WasmBindingPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply by lazy { TargetType.WASM in project.moduleProperties.targets }



    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate()  = with(project){
        moduleProperties.targets
            .filter { target -> target.type.isDescendantOf(TargetType.WASM) }
            .forEach { target->target.add() }
    }

    private fun applySettings()=with(project){
        configureKotlinJsTarget< KotlinWasmJsTargetDsl>()
        configureJsTestTasks<KotlinWasmJsTargetDsl>()
    }
}

