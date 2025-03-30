package gradle.plugins.kotlin.serialization.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal  class SerializationSettings{

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.plugin.serialization") {

        }
}
