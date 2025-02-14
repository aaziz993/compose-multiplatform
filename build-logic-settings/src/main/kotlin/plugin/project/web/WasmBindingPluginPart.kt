package plugin.project.web

import org.gradle.api.Project
import org.jetbrains.amper.frontend.Platform
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.amper.gradle.contains
import org.jetbrains.amper.gradle.kmpp.KotlinAmperNamingConvention
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.project.KMPEAware

private val Project.wasmSupported: Boolean
    get() = (findProperty("wasm.supported") as String?)?.toBoolean() == true

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class WasmBindingPluginPart(
    ctx: PluginPartCtx,
) : WebAwarePart(ctx,"wasmJs") {

    override val needToApply by lazy { Platform.WASM in module && project.wasmSupported }

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        super.applyBeforeEvaluate()
    }
}

