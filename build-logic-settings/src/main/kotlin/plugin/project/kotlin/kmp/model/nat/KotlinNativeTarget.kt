package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasBinaries
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions

@Serializable
internal abstract class KotlinNativeTarget : KotlinTarget,
    plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions<KotlinNativeCompilerOptions>,
    HasBinaries<KotlinNativeBinaryContainer?> {

    abstract override val compilations: List<KotlinNativeCompilation>?

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinTarget>.applyTo(named)

        named as KotlinNativeTarget

        super<HasConfigurableKotlinCompilerOptions>.applyTo(named)

        binaries?.let { binaries ->
            named.binaries {
                binaries.framework?.let { framework ->
                    framework {
                        framework.applyTo(this)
                    }
                }
            }
        }
    }
}
