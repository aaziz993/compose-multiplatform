package plugin.project.compose.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.DesktopExtension

@Serializable
internal data class DesktopExtension(
    val application: JvmApplication? = null,
    val nativeApplication: NativeApplication? = null,
) {

    context(Project)
    fun applyTo(extension: DesktopExtension) {
        application?.applyTo(extension.application)

        nativeApplication?.let { nativeApplication ->
            extension.nativeApplication {
                nativeApplication.applyTo(this)
            }
        }
    }
}
