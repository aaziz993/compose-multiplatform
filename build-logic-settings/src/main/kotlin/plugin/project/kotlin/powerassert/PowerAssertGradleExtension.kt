package plugin.project.kotlin.powerassert

import gradle.moduleProperties
import gradle.powerAssert
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradlePlugin

internal fun Project.configurePowerAssertGradleExtension() =
    plugins.withType<PowerAssertGradlePlugin> {
        moduleProperties.settings.kotlin.powerAssert.let { powerAssert ->
            powerAssert {
                functions tryAssign powerAssert.functions
                includedSourceSets tryAssign powerAssert.includedSourceSets
            }
        }
    }

