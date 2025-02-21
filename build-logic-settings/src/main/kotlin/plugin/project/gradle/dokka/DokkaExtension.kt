package plugin.project.gradle.dokka

import gradle.dokka
import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi

@OptIn(InternalDokkaGradlePluginApi::class)
internal fun Project.configureDokkaExtension() =
    pluginManager.withPlugin(libs.plugins.dokka.get().pluginId) {
        moduleProperties.settings.gradle.dokka.let { dokka ->
            dokka {
                dokka.applyTo(this)
            }
        }
    }






