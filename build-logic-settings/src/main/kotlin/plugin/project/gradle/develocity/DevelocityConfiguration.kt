package plugin.project.gradle.develocity

import com.gradle.develocity.agent.gradle.DevelocityPlugin
import gradle.id
import gradle.isCI
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.tryAssign
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity
import org.gradle.kotlin.dsl.withType

internal fun Settings.configureDevelocityConfiguration() =
    pluginManager.withPlugin(libs.plugins.plugin("develocity").id) {
        projectProperties.settings.gradle.develocity.let { develocity ->
            develocity {
                develocity.applyTo(this)
            }
        }
    }



