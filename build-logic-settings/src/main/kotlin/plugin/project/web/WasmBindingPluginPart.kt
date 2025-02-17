package plugin.project.web

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.amper.frontend.Platform
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.amper.gradle.contains
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import plugin.project.web.js.JsBindingPluginPart

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class WasmBindingPluginPart(ctx: PluginPartCtx) : WebAwarePart(ctx) {

    override val needToApply by lazy { Platform.WASM in module }

    override val target: KotlinJsTargetDsl by lazy {
        kotlinMPE.wasmJs(module.artifactPlatforms.single { it == Platform.WASM }.targetName)
    }

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        super.applyBeforeEvaluate()
    }
}

