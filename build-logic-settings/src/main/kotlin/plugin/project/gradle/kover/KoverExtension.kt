package plugin.project.gradle.kover

import gradle.id
import gradle.kover
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKoverExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("kover").id) {
       projectProperties.plugins.kover.let { kover ->
            kover {
                kover.applyTo(this)
            }
        }
    }


