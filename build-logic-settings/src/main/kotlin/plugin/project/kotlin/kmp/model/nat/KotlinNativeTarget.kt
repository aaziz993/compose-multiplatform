package plugin.project.kotlin.kmp.model.nat

import gradle.kotlin
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.gradle.model.HasBinaries
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions
import org.gradle.kotlin.dsl.withType

@Serializable
internal abstract class KotlinNativeTarget : KotlinTarget,
    HasConfigurableKotlinCompilerOptions<KotlinNativeCompilerOptions>,
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

    context(Project)
    override fun applyTo() =
        super<KotlinTarget>.applyTo(kotlin.targets.withType<KotlinNativeTarget>())
}
