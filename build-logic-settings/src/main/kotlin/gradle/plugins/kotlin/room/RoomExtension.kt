package gradle.plugins.kotlin.room

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.room
import gradle.accessors.settings
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface RoomExtension {

    // User variant / target match pattern and its copy task. Multiple variant / target annotation
    // processing tasks can be finalized by the same copy task.
    val schemaDirectories: Set<@Serializable(with = SchemaDirectoryTransformingSerializer::class) SchemaDirectory>?

    /** Causes Room annotation processor to generate Kotlin code instead of Java. */
    val generateKotlin: Boolean?

    context(project: Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("room").id) {
            schemaDirectories?.forEach { (matchName, path) ->
                project.room.schemaDirectory(matchName, path)
            }

            project.room::generateKotlin trySet generateKotlin
        }
}
