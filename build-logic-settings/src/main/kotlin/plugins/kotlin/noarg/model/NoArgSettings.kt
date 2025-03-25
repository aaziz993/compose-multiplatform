@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugins.kotlin.noarg.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.noarg.NoArgExtension
import gradle.project.EnabledSettings
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
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("noarg").id) {
            super.applyTo()
        }
}
