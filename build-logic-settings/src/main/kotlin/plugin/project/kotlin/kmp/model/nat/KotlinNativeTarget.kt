package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.configure

@Serializable
internal abstract class KotlinNativeTarget : KotlinTarget,
    plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions<KotlinNativeCompilerOptions>,
    HasBinaries<KotlinNativeBinaryContainer?> {

    abstract override val compilations: List<KotlinNativeCompilation>?

    context(Project)
    override fun applyTo(targets: NamedDomainObjectCollection<out org.jetbrains.kotlin.gradle.plugin.KotlinTarget>) {
        super<KotlinTarget>.applyTo(targets)

        targets.configure {
            this as KotlinNativeTarget

            this@KotlinNativeTarget.compilerOptions?.applyTo(compilerOptions)

            this@KotlinNativeTarget.binaries?.let { binaries ->
                binaries {
                    binaries.framework?.let { framework ->
                        framework {
                            framework.applyTo(container {  this})
                        }
                    }
                }
            }
        }
    }
}
