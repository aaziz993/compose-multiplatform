package plugin.project.gradle.kover

import gradle.kover
import gradle.libs
import gradle.moduleProperties
import gradle.tryAssign
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import plugin.project.gradle.kover.model.KoverReportFiltersConfig

internal fun Project.configureKoverExtension() =
    pluginManager.withPlugin(libs.plugins.kover.get().pluginId) {
        moduleProperties.settings.gradle.kover.let { kover ->
            kover {
                kover.applyTo(this)
            }
        }
    }


