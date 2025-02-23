package plugin.project.compose.desktop.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.NativeApplication

@Serializable
internal data class NativeApplication(
    val distributions: NativeApplicationDistributions? = null,
) {

    context(Project)
    fun applyTo(application: NativeApplication) {
        distributions?.applyTo(application.distributions)
    }
}
