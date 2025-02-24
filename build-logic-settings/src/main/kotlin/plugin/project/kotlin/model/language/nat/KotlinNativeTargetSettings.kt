package plugin.project.kotlin.model.language.nat

import kotlinx.serialization.Serializable
import plugin.project.model.EnabledSettings

@Serializable
internal data class KotlinNativeTargetSettings(
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val enabled: Boolean = true
) : KotlinNativeTarget, EnabledSettings
