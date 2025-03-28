package gradle.plugins.compose.android

import gradle.accessors.android
import gradle.accessors.catalog.libs
import gradle.accessors.compose
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal class AndroidExtension {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("compose.multiplatform").id) {
            project.compose.android {

            }
        }
}
