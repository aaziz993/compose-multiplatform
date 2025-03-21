package gradle.plugins.cmp.desktop

import gradle.accessors.compose
import gradle.accessors.desktop
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.cmp.desktop.application.JvmApplication
import gradle.plugins.cmp.desktop.nativeapplication.NativeApplication
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DesktopExtension(
    val application: JvmApplication? = null,
    val nativeApplication: NativeApplication? = null,
) {

    context(Project)
    fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        application?.applyTo(compose.desktop.application)
        nativeApplication?.applyTo(compose.desktop.nativeApplication)
    }
}
