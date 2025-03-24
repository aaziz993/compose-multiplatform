package gradle.plugins.cmp.desktop.application.buildtype

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationBuildTypes

@Serializable
internal data class JvmApplicationBuildTypes(
    val release: JvmApplicationBuildType? = null
) {

    context(project: Project)
    fun applyTo(receiver: JvmApplicationBuildTypes) {
        release?.applyTo(receiver.release)
    }
}
