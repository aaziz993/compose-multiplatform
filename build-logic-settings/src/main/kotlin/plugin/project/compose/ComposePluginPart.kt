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
import org.gradle.api.Plugin
import plugin.project.compose.desktop.configureDesktopExtension

public class ComposePluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.compose.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(libs.plugins.compose.multiplatform.get().pluginId)
            plugins.apply(libs.plugins.compose.compiler.get().pluginId)

            if (moduleProperties.application) {
                configureDesktopExtension()
            }

            configureResourcesExtension()
        }
    }
}
