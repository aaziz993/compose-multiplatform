package gradle.plugins.kmp

import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinBaseExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal interface KotlinMultiplatformExtension :
    KotlinBaseExtension<KotlinMultiplatformExtension>,
    HasConfigurableKotlinCompilerOptions<KotlinMultiplatformExtension, KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    context(Project)
    override fun applyTo(recipient: KotlinMultiplatformExtension) {
        super<KotlinBaseExtension>.applyTo(recipient)

        withSourcesJar?.let(recipient::withSourcesJar)
    }
}
