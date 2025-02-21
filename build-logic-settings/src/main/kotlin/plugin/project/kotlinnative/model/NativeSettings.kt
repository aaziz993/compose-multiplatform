package plugin.project.kotlinnative.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
internal data class NativeSettings(
    val binaries: Binaries? = null,
) {

    context(Project)
    fun applyTo(target: KotlinNativeTarget) {
        binaries?.let { binaries ->
            target.binaries {
                binaries.framework?.let { framework ->
                    framework {
                        framework.applyTo(this)
                    }
                }
            }
        }
    }
}
