package plugin.project.kotlin.kmp.model.nat.linux

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilerOptions

@Serializable
@SerialName("linuxArm64")
internal data class KotlinLinuxArm64Target(
    override val targetName: String = "",
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinLinuxTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::linuxArm64) ?: kotlin.linuxArm64())
    }
}
