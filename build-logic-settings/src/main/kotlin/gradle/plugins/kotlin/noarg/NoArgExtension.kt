@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.noarg

import gradle.accessors.noArg
import gradle.collection.tryAddAll
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class NoArgExtension(
    val myAnnotations: List<String>? = null,
    val myPresets: List<String>? = null,
    val invokeInitializers: Boolean? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.plugin.noarg") {
            myAnnotations?.let(project.noArg::annotations)
            project.noArg.myPresets tryAddAll myPresets
            project.noArg::invokeInitializers trySet invokeInitializers
        }
}
