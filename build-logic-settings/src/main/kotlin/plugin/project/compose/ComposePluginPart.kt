@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.amper.frontend.schema.ProductType
import org.jetbrains.amper.gradle.android.AndroidAwarePart
import org.jetbrains.amper.gradle.base.AmperNamingConventions
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.project.compose.desktop.configureDesktopExtension

public class ComposePluginPart(ctx: PluginPartCtx) : KMPEAware, AmperNamingConventions, AndroidAwarePart(ctx) {

    override val kotlinMPE: KotlinMultiplatformExtension =
        project.extensions.getByType<KotlinMultiplatformExtension>()

    override val needToApply: Boolean by lazy {
        module.leafFragments.any { it.settings.compose.enabled }
    }

    // Highly dependent on compose version and ABI.
    // Need to implement API on compose plugin side.
    override fun applyBeforeEvaluate(): Unit = with(project) {
        plugins.apply(project.settings.libs.plugins.plugin("compose-multiplatform").id)
        plugins.apply(project.settings.libs.plugins.plugin("compose-compiler").id)

        if (module.type == ProductType.JVM_APP) {
            // Configure desktop
            configureDesktopExtension()
        }

        // Adjust resources.
        configureResourcesExtension()
    }
}
