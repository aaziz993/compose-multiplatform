package plugin.project.kotlin.allopen

import gradle.allOpen
import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

internal fun Project.configureAllOpenExtension() =
    pluginManager.withPlugin(libs.plugins.allopen.get().pluginId) {
        moduleProperties.settings.kotlin.allOpen.let { allOpen ->
            allOpen(allOpen::applyTo)
        }
    }
