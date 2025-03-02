package plugin.project.compose.android.model

import gradle.android
import gradle.compose
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal class AndroidExtension {

    context(Project)
    fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        compose.android {

        }
    }
}
