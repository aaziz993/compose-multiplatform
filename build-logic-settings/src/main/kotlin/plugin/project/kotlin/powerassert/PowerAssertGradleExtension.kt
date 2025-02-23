package plugin.project.kotlin.powerassert

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.powerAssert
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configurePowerAssertGradleExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("power.assert").id) {
       projectProperties.plugins.powerAssert.let { powerAssert ->
            powerAssert(powerAssert::applyTo)
        }
    }

