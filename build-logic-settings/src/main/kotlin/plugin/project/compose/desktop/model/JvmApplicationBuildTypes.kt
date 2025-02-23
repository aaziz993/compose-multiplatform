package plugin.project.compose.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationBuildTypes

@Serializable
internal data class JvmApplicationBuildTypes(
    val release: JvmApplicationBuildType? = null
) {

    context(Project)
    fun applyTo(buildTypes: JvmApplicationBuildTypes) {
        release?.let { release ->
            buildTypes.release {
                release.proguard?.let { proguard ->
                    proguard {
                        proguard.applyTo(this)
                    }
                }
            }
        }
    }
}
