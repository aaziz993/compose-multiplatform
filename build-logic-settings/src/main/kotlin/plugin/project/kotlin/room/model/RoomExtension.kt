package plugin.project.kotlin.room.model

import gradle.room
import gradle.trySet
import org.gradle.api.Project

internal interface RoomExtension {

    // User variant / target match pattern and its copy task. Multiple variant / target annotation
    // processing tasks can be finalized by the same copy task.
    val schemaDirectories: Set<String>?

    /** Causes Room annotation processor to generate Kotlin code instead of Java. */
    val generateKotlin: Boolean?

    context(Project)
    fun applyTo() {
        schemaDirectories?.forEach(room::schemaDirectory)
        room::generateKotlin trySet generateKotlin
    }
}

