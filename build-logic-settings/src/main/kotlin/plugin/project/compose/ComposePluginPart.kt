@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import gradle.id
import gradle.libs
import gradle.moduleProperties
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.amper.frontend.schema.ProductType
import org.jetbrains.amper.gradle.android.AndroidAwarePart
import org.jetbrains.amper.gradle.base.AmperNamingConventions
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.project.BindingPluginPart
import plugin.project.compose.desktop.configureDesktopExtension

public class ComposePluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.compose.enabled
    }

    // Highly dependent on compose version and ABI.
    // Need to implement API on compose plugin side.
    override fun applyBeforeEvaluate(): Unit = with(project) {
        plugins.apply(project.settings.libs.plugins.plugin("compose-multiplatform").id)
        plugins.apply(project.settings.libs.plugins.plugin("compose-compiler").id)

        if (moduleProperties.application) {
            // Configure desktop
            configureDesktopExtension()
        }

        // Adjust resources.
        configureResourcesExtension()
    }
}
