package plugin.project.kotlin.powerassert

import gradle.libs
import gradle.moduleProperties
import gradle.powerAssert
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradlePlugin

internal fun Project.configurePowerAssertGradleExtension() =
    pluginManager.withPlugin(libs.plugins.power.assert.get().pluginId) {
        moduleProperties.settings.kotlin.powerAssert.let { powerAssert ->
            powerAssert(powerAssert::applyTo)
        }
    }

