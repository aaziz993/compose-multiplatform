package plugin.project.kotlin.model.language.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
internal data class KotlinNativeTarget(
    override val binaries: KotlinNativeBinaryContainer? = null,
    val compilerOptions: KotlinNativeCompilerOptions? = null,
) : HasBinaries<KotlinNativeBinaryContainer> {

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
