package plugin.project.gradle.dokka

import gradle.dokka
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi

@OptIn(InternalDokkaGradlePluginApi::class)
internal fun Project.configureDokkaExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("dokka").id) {
       projectProperties.plugins.dokka.let { dokka ->
            dokka {
                dokka.applyTo(this)
            }
        }
    }






