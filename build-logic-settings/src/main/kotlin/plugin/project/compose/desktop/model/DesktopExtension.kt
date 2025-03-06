package plugin.project.compose.desktop.model

import gradle.compose
import gradle.desktop
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DesktopExtension(
    val application: JvmApplication? = null,
    val nativeApplication: NativeApplication? = null,
) {

    context(Project)
    fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        compose.desktop {
            this@DesktopExtension.application?.applyTo(application)

            this@DesktopExtension.nativeApplication?.let { nativeApplication ->
                nativeApplication {
                    nativeApplication.applyTo(this)
                }
            }
        }
    }
}
