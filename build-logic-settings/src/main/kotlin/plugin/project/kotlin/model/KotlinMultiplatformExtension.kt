package plugin.project.kotlin.model

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal interface KotlinMultiplatformExtension : KotlinBaseExtension,
    HasConfigurableKotlinCompilerOptions<KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    fun applyTo(extension: KotlinMultiplatformExtension) {
        super<KotlinBaseExtension>.applyTo(extension)
        compilerOptions?.applyTo(extension.compilerOptions)
        withSourcesJar?.let(extension::withSourcesJar)
    }
}
