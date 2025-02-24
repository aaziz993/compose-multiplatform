package plugin.project.web.js.karakum

import gradle.id
import gradle.karakum
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKarakumExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
        projectProperties.karakum.applyTo(karakum)
    }
