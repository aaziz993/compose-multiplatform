package plugin.project.compose.desktop

import gradle.compose
import gradle.desktop
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension

internal fun Project.configureDesktopExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        compose {
            projectProperties.compose.desktop.let { desktop ->
                desktop {
                    desktop.applyTo(this)
                }
            }
        }
    }
