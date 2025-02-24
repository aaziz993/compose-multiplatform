package plugin.project.kotlin.kmp.model

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.project.kotlin.model.KotlinBaseExtension
import plugin.project.kotlin.model.language.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.language.KotlinCommonCompilerOptions

internal interface KotlinMultiplatformExtension : KotlinBaseExtension,
    HasConfigurableKotlinCompilerOptions<KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    fun applyTo(extension: KotlinMultiplatformExtension) {
        super<KotlinBaseExtension>.applyTo(extension)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(extension)

        withSourcesJar?.let(extension::withSourcesJar)
    }
}
