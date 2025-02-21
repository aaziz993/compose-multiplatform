@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.kotlin.noarg.model

import gradle.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class NoArgSettings(
    override val myAnnotations: List<String>? = null,
    override val myPresets: List<String>? = null,
    val invokeInitializers: Boolean? = null,
    val enabled: Boolean = true,
) : NoArgExtension {

    fun applyTo(extension: org.jetbrains.kotlin.noarg.gradle.NoArgExtension) {
        myAnnotations?.let(extension::annotations)
        myPresets?.let(extension.myPresets::addAll)
        extension::invokeInitializers trySet invokeInitializers
    }
}
