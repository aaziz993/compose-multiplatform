package plugin.project.kotlin.model.language.android

import kotlinx.serialization.Serializable
import plugin.project.kotlin.model.language.jvm.KotlinJvmCompilerOptions
import plugin.project.model.EnabledSettings

@Serializable
internal data class KotlinAndroidTargetSettings(
    override val publishLibraryVariants: List<String>? = null,
    override val publishAllLibraryVariants: Boolean? = null,
    override val publishLibraryVariantsGroupedByFlavor: Boolean? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val enabled: Boolean = true,
) : KotlinAndroidTarget, EnabledSettings
