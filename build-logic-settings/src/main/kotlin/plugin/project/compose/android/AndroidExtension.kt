package plugin.project.compose.android

import gradle.android
import gradle.compose
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureAndroidExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
//        projectProperties.compose.android.let { android ->
            compose.android {

            }
//        }
    }
