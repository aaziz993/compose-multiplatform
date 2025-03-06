package gradle.model.kmp

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import gradle.model.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.model.kotlin.KotlinBaseExtension
import gradle.model.kotlin.KotlinCommonCompilerOptions

internal interface KotlinMultiplatformExtension :
    KotlinBaseExtension, HasConfigurableKotlinCompilerOptions<KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    context(Project)
    fun applyTo(extension: KotlinMultiplatformExtension) {
        super<KotlinBaseExtension>.applyTo(extension)
        compilerOptions?.applyTo(extension.compilerOptions as org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions)
        withSourcesJar?.let(extension::withSourcesJar)
    }
}
