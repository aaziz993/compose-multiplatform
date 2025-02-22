package plugin.project.kotlin.powerassert

import gradle.libs
import gradle.projectProperties
import gradle.powerAssert
import gradle.settings
import org.gradle.api.Project

internal fun Project.configurePowerAssertGradleExtension() =
    pluginManager.withPlugin(libs.plugins.power.assert.get().pluginId) {
       projectProperties.plugins.powerAssert.let { powerAssert ->
            powerAssert(powerAssert::applyTo)
        }
    }

