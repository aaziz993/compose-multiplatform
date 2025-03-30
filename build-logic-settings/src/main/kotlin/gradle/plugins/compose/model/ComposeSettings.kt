package gradle.plugins.compose.model

import gradle.plugins.compose.android.AndroidExtension
import gradle.plugins.compose.desktop.DesktopExtension
import gradle.plugins.compose.resources.ResourcesExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class ComposeSettings(
    val desktop: DesktopExtension = DesktopExtension(),
    val android: AndroidExtension = AndroidExtension(),
    val resources: ResourcesExtension = ResourcesExtension(),
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.compose") {
            desktop.applyTo()
            android.applyTo()
            resources.applyTo()
        }
}
