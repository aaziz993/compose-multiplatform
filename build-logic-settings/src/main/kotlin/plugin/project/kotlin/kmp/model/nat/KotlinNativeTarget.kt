package plugin.project.kotlin.kmp.model.nat

import gradle.containerize
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectCollection
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
    override fun applyTo(target: org.jetbrains.kotlin.gradle.plugin.KotlinTarget) {
        super<KotlinTarget>.applyTo(target)

        target as KotlinNativeTarget

        super<HasConfigurableKotlinCompilerOptions>.applyTo(target)

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
