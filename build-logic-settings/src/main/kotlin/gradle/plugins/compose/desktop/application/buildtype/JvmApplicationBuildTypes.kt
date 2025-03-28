package gradle.plugins.compose.desktop.application.buildtype

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationBuildTypes

@Serializable
internal data class JvmApplicationBuildTypes(
    val release: JvmApplicationBuildType? = null
) {

    context(Project)
    fun applyTo(receiver: JvmApplicationBuildTypes) {
        release?.applyTo(receiver.release)
    }
}
