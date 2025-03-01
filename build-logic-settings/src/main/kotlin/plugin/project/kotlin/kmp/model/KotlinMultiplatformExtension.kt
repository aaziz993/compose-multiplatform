package plugin.project.kotlin.kmp.model

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal interface KotlinMultiplatformExtension : plugin.project.kotlin.model.KotlinBaseExtension,
    plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions<plugin.project.kotlin.model.KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    fun applyTo(extension: KotlinMultiplatformExtension) {
        super<plugin.project.kotlin.model.KotlinBaseExtension>.applyTo(extension)
        compilerOptions?.applyTo(extension.compilerOptions)
        withSourcesJar?.let(extension::withSourcesJar)
    }
}
