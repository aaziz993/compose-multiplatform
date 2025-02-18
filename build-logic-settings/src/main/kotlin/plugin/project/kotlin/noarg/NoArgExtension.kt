package plugin.project.kotlin.noarg

import gradle.allOpen
import gradle.amperModuleExtraProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.noarg.gradle.NoArgGradleSubplugin

internal fun Project.configureNoArgExtension() =
    plugins.withType<NoArgGradleSubplugin> {
        amperModuleExtraProperties.settings.kotlin.allOpen.let { allOpen ->
            allOpen {
                allOpen.myAnnotations?.forEach(::annotation)
                allOpen.myPresets?.forEach(::preset)
            }
        }
    }
