package plugin.project.web.js.karakum

import gradle.moduleProperties
import gradle.karakum
import gradle.tryAssign
import io.github.sgrishchenko.karakum.gradle.plugin.KarakumPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKarakumExtension() =
    plugins.withType<KarakumPlugin> {
        moduleProperties.settings.web.karakum.let { karakum ->
            karakum {
                configFile tryAssign karakum.configFile?.let(::file)
                extensionSource tryAssign karakum.extensionSource?.let(layout.projectDirectory::dir)?.asFileTree
            }
        }
    }
