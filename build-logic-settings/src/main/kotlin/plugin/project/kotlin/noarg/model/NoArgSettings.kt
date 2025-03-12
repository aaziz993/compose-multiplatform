@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.kotlin.noarg.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.kotlin.noarg.NoArgExtension
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class NoArgSettings(
    override val myAnnotations: List<String>? = null,
    override val myPresets: List<String>? = null,
    override val invokeInitializers: Boolean? = null,
    override val enabled: Boolean = true,
) : NoArgExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("noarg").id) {
            super.applyTo()
        }
}
