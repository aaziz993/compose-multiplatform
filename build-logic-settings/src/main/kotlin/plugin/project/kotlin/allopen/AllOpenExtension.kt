package plugin.project.kotlin.allopen

import gradle.allOpen
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

internal fun Project.configureAllOpenExtension() =
    plugins.withType<AllOpenGradleSubplugin> {
        moduleProperties.settings.kotlin.allOpen.let { allOpen ->
            allOpen {
                allOpen.myAnnotations?.forEach(::annotation)
                allOpen.myPresets?.forEach(::preset)
            }
        }
    }
