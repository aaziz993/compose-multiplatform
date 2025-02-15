package plugin.project.web.js.karakum

import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import gradle.amperModuleExtraProperties

internal fun Project.configureKarakumExtension(extension: KarakumExtension) =
    with(amperModuleExtraProperties.settings.web.framework) {
        extension.apply {
            karakum.configFile?.let { configFile = file(it) }
        }
    }
