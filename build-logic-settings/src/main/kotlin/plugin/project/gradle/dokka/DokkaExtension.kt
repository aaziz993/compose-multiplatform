package plugin.project.gradle.dokka

import gradle.dokka
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi

@OptIn(InternalDokkaGradlePluginApi::class)
internal fun Project.configureDokkaExtension() =
    pluginManager.withPlugin(libs.plugins.dokka.get().pluginId) {
       settings.projectProperties.plugins.dokka.let { dokka ->
            dokka {
                dokka.applyTo(this)
            }
        }
    }






