package plugin.project.web.js.karakum

import gradle.amperModuleExtraProperties
import gradle.tryAssign
import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import org.gradle.api.Project

internal fun Project.configureKarakumExtension(extension: KarakumExtension) =
    with(amperModuleExtraProperties.settings.web) {
        extension.apply {
            configFile tryAssign karakum.configFile?.let(::file)
        }
    }
