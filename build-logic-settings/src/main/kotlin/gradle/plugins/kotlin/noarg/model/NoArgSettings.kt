@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.noarg.model

import gradle.accessors.catalog.libs

import gradle.accessors.settings
import gradle.plugins.kotlin.noarg.NoArgExtension
import gradle.api.EnabledSettings
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
        project.pluginManager.withPlugin(project.settings.libs.plugin("noarg").id) {
            super.applyTo()
        }
}
