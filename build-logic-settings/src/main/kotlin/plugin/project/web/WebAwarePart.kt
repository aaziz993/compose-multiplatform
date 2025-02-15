package plugin.project.web

import org.gradle.api.NamedDomainObjectCollection
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.KMPEAware

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal abstract class WebAwarePart(
    ctx: PluginPartCtx,
) : KMPEAware, BindingPluginPart by ctx {

    override val kotlinMPE: KotlinMultiplatformExtension =
        project.extensions.getByType(KotlinMultiplatformExtension::class.java)

    protected abstract val target: NamedDomainObjectCollection<out KotlinJsTargetDsl>

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        adjustTarget()
    }

    private fun adjustTarget() = with(project) {
        target.all {
            configureKotlinJsTarget(this)
            configureJsTestTasks(this)
        }
    }
}
