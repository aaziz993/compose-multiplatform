package plugin.project.amperlike

import org.jetbrains.amper.frontend.Platform
import org.jetbrains.amper.gradle.android.AndroidAmperNamingConvention
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.project.amper.KMPEAware

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class WasmBindingPluginPart(
    ctx: AmperLikePluginPartCtx,
) : KMPEAware, AmperLikeBindingPluginPart by ctx {

    override val kotlinMPE: KotlinMultiplatformExtension =
        project.extensions.getByType(KotlinMultiplatformExtension::class.java)

    override val needToApply by lazy { Platform.WASM in module }

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {

//        adjustCompilations()
//        applySettings()
        adjustWasmSourceSets()
    }

    override fun applyAfterEvaluate() {
        // Be sure our adjustments are made lastly.
        project.afterEvaluate {
            adjustWasmSourceSets()
        }
    }

    private fun adjustWasmSourceSets() = with(AndroidAmperNamingConvention) {
    }
}
