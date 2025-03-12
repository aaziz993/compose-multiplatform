@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.kotlin.noarg.model

import gradle.id
import gradle.libs
import gradle.plugins.kotlin.noarg.NoArgExtension
import gradle.plugins.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
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
