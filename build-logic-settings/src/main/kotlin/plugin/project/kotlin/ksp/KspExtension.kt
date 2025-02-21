package plugin.project.kotlin.ksp

import com.google.devtools.ksp.gradle.KspGradleSubplugin
import gradle.ksp
import gradle.libs
import gradle.moduleProperties
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKspExtension() =
    pluginManager.withPlugin(libs.plugins.ksp.get().pluginId) {
        moduleProperties.settings.kotlin.ksp.let { ksp ->
            ksp(ksp::applyTo)
        }
    }

