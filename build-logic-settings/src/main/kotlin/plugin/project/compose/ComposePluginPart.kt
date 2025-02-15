@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.amper.frontend.schema.ProductType
import org.jetbrains.amper.gradle.android.AndroidAwarePart
import org.jetbrains.amper.gradle.base.AmperNamingConventions
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import gradle.chooseComposeVersion
import plugin.project.KMPEAware
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
        val composeVersion = chooseComposeVersion(model)!!

        plugins.apply("org.jetbrains.kotlin.plugin.compose")
        plugins.apply("org.jetbrains.compose")

        module.rootFragment.kotlinSourceSet?.apply {
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:$composeVersion")
                implementation("org.jetbrains.compose.components:components-resources:$composeVersion")
            }
        }

        extensions.configure<ComposeExtension> {
            if (module.type == ProductType.JVM_APP) {
                // Configure desktop
                extensions.configure<DesktopExtension>(::configureDesktopExtension)
            }

            // Adjust task.
            extensions.configure<ResourcesExtension> {
                configureResourcesExtension(this)
            }
        }
    }
}
