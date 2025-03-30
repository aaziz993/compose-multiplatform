package gradle.plugins.compose.android

import gradle.accessors.android
import gradle.accessors.compose
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal class AndroidExtension {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.compose") {
            project.compose.android {

            }
        }
}
