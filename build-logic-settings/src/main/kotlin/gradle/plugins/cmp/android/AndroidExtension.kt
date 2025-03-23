package gradle.plugins.cmp.android

import gradle.accessors.android
import gradle.accessors.compose
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal class AndroidExtension {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
            compose.android {

            }
        }
}
