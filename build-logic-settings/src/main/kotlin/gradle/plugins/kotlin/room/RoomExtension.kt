package gradle.plugins.kotlin.room

import gradle.accessors.catalog.libs
import gradle.accessors.room
import gradle.accessors.settings
import gradle.api.trySet
import org.gradle.api.Project

internal interface RoomExtension {

    // User variant / target match pattern and its copy task. Multiple variant / target annotation
    // processing tasks can be finalized by the same copy task.
    val schemaDirectories: Set<SchemaDirectory>?

    /** Causes Room annotation processor to generate Kotlin code instead of Java. */
    val generateKotlin: Boolean?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("room").id) {
            schemaDirectories?.forEach { (matchName, path) ->
                project.room.schemaDirectory(matchName, path)
            }

            project.room::generateKotlin trySet generateKotlin
        }
}
