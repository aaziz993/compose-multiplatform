package plugin.project.compose.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationBuildType

@Serializable
internal data class JvmApplicationBuildType(
    val proguard: ProguardSettings? = null,
) {

    context(Project)
    fun applyTo(buildType: JvmApplicationBuildType) {
        proguard?.applyTo(buildType.proguard)
    }
}
