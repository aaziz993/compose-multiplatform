package gradle.plugins.compose.desktop

import gradle.accessors.compose
import gradle.accessors.desktop
import gradle.plugins.compose.desktop.application.JvmApplication
import gradle.plugins.compose.desktop.nativeapplication.NativeApplication
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DesktopExtension(
    val application: JvmApplication? = null,
    val nativeApplication: NativeApplication? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.compose") {
            application?.applyTo(project.compose.desktop.application)
            nativeApplication?.applyTo(project.compose.desktop.nativeApplication)
        }
}
