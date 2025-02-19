package plugin.project.kotlin.ksp

import com.google.devtools.ksp.gradle.KspGradleSubplugin
import gradle.moduleProperties
import gradle.ksp
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKspExtension() =
    plugins.withType<KspGradleSubplugin> {
        moduleProperties.settings.kotlin.ksp2.let { ksp ->
            ksp {
                useKsp2 tryAssign ksp.useKsp2
                ksp.commandLineArgumentProviders?.let { commandLineArgumentProviders ->
                    arg { commandLineArgumentProviders }
                }
                ksp.excludedProcessors?.forEach(::excludeProcessor)
                ksp.excludedSources?.forEach(excludedSources::setFrom)
                ksp.arguments?.forEach { (key, value) -> arg(key, value) }
                ::allWarningsAsErrors trySet ksp.allWarningsAsErrors
            }
        }
    }

