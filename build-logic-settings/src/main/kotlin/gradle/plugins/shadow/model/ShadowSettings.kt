package gradle.plugins.shadow.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal class ShadowSettings {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("com.gradleup.shadow") {

        }
}
