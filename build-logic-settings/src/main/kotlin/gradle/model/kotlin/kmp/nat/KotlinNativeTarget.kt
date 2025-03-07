package gradle.model.kotlin.kmp.nat

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import gradle.model.HasBinaries
import gradle.model.kotlin.kmp.KotlinTarget
import gradle.model.kotlin.HasConfigurableKotlinCompilerOptions

@Serializable
internal abstract class KotlinNativeTarget : KotlinTarget,
    HasConfigurableKotlinCompilerOptions<KotlinNativeCompilerOptions>,
    HasBinaries<KotlinNativeBinaryContainer?> {

    abstract override val compilations: List<KotlinNativeCompilation>?

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinTarget>.applyTo(named)

        named as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

        super<HasConfigurableKotlinCompilerOptions>.applyTo(named)

        binaries?.let { binaries ->
            named.binaries {
                binaries.applyTo(this)
            }
        }
    }

    context(Project)
    override fun applyTo() =
        super<KotlinTarget>.applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>())
}

@Serializable
@SerialName("native")
internal data class KotlinNativeTargetImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget() {

    override val targetName: String
        get() = ""
}
