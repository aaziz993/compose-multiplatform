@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.kotlin.noarg.model

import gradle.id
import gradle.libs
import gradle.noArg
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class NoArgSettings(
    override val myAnnotations: List<String>? = null,
    override val myPresets: List<String>? = null,
    val invokeInitializers: Boolean? = null,
    override val enabled: Boolean = true,
) : NoArgExtension, EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("noarg").id) {
            myAnnotations?.let(noArg::annotations)
            myPresets?.let(noArg.myPresets::addAll)
            noArg::invokeInitializers trySet invokeInitializers
        }
}
