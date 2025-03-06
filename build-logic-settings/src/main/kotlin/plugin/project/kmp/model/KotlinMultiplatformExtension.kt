package plugin.project.kmp.model

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.KotlinBaseExtension
import plugin.project.kotlin.model.KotlinCommonCompilerOptions

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
