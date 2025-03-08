package gradle.model.kotlin.kmp

import gradle.model.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.model.kotlin.KotlinBaseExtension
import gradle.model.kotlin.KotlinCommonCompilerOptions
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal interface KotlinMultiplatformExtension :
    KotlinBaseExtension, HasConfigurableKotlinCompilerOptions<KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    context(Project)
    override fun applyTo(extension: org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension) {
        super<KotlinBaseExtension>.applyTo(extension)

        extension as KotlinMultiplatformExtension

        compilerOptions?.applyTo(extension.compilerOptions as org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions)
        withSourcesJar?.let(extension::withSourcesJar)
    }
}
