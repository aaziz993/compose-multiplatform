package plugin.project.kotlin.kmp.model.nat.apple.tvos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilerOptions


@Serializable
@SerialName("tvosArm64")
internal data class KotlinTvosArm64Target(
    override val targetName: String = "",
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinTvosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::tvosArm64) ?: kotlin.tvosArm64())
    }
}
