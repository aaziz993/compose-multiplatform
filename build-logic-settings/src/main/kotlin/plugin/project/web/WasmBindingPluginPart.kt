package plugin.project.web

import gradle.kotlin
import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import plugin.project.BindingPluginPart
import plugin.project.model.hasWasmJs
import plugin.project.model.wasmjs

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class WasmBindingPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply by lazy { project.moduleProperties.targets.hasWasmJs }



    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate()  = with(project){
        moduleProperties.targets.wasmjs.forEach { target -> target.applyTo(kotlin) }
    }

    private fun applySettings()=with(project){
        configureKotlinJsTarget< KotlinWasmJsTargetDsl>()
        configureJsTestTasks<KotlinWasmJsTargetDsl>()
    }
}

