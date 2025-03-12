package gradle.plugins.kmp.nat

import gradle.kotlin
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kmp.KotlinTarget
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinNativeTarget : KotlinTarget,
    HasConfigurableKotlinCompilerOptions<KotlinNativeCompilerOptions>,
    HasBinaries<KotlinNativeBinaryContainer?> {

    abstract override val compilations: List<KotlinNativeCompilation>?

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinTarget>.applyTo(named)

        named as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

        super<HasConfigurableKotlinCompilerOptions>.applyTo(named)

        binaries?.applyTo(named.binaries)
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
