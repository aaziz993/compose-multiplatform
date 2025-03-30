package gradle.plugins.kotlin.allopen

import gradle.accessors.allOpen
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AllOpenExtension(
    val myAnnotations: List<String>? = null,
    val myPresets: List<String>? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.plugin.allopen") {
            myAnnotations?.let(project.allOpen::annotations)
            myPresets?.forEach(project.allOpen::preset)
        }
}
