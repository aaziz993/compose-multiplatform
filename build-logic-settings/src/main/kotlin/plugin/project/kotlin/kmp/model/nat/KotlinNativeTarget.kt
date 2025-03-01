package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.KotlinTarget

@Serializable
internal abstract class KotlinNativeTarget : KotlinTarget,
    plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions<KotlinNativeCompilerOptions>,
    HasBinaries<KotlinNativeBinaryContainer?> {

    abstract override val compilations: List<KotlinNativeCompilation>?

    context(Project)
    protected fun applyTo(target: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget) {
        super<KotlinTarget>.applyTo(target)

        compilerOptions?.applyTo(target.compilerOptions)

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
