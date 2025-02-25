package plugin.project.kotlin.model.language

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal interface KotlinMultiplatformExtension : KotlinBaseExtension,
    HasConfigurableKotlinCompilerOptions<KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    fun applyTo(extension: KotlinMultiplatformExtension) {
        super<KotlinBaseExtension>.applyTo(extension)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(extension)
        withSourcesJar?.let(extension::withSourcesJar)
    }
}
