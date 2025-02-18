package plugin.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

@Serializable
internal data class Dependency(

    val notation: String
) {

    fun Settings.toDependencyNotation(): Any {
    }

    fun Project.toDependencyNotation(): Any {
    }

    private fun toDependencyNotation(): Any {
    }
}
