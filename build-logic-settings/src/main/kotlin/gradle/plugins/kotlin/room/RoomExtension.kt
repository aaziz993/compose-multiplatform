package gradle.plugins.kotlin.room

import gradle.accessors.room
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class RoomExtension(
    // User variant / target match pattern and its copy task. Multiple variant / target annotation
    // processing tasks can be finalized by the same copy task.
    val schemaDirectories: Set<SchemaDirectory>? = null,
    /** Causes Room annotation processor to generate Kotlin code instead of Java. */
    val generateKotlin: Boolean? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("androidx.room") {
            schemaDirectories?.forEach { (matchName, path) ->
                project.room.schemaDirectory(matchName, path)
            }

            project.room::generateKotlin trySet generateKotlin
        }
}
