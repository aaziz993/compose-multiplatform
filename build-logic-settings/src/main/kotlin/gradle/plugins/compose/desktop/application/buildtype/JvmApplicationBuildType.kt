package gradle.plugins.compose.desktop.application.buildtype

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationBuildType

@Serializable
internal data class JvmApplicationBuildType(
    val proguard: ProguardSettings? = null,
) {

    context(Project)
    fun applyTo(receiver: JvmApplicationBuildType) {
        proguard?.applyTo(receiver.proguard)
    }
}
