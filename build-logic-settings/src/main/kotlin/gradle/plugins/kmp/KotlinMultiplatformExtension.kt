package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinBaseExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension

internal interface KotlinMultiplatformExtension :
    KotlinBaseExtension<KotlinMultiplatformExtension>,
    HasConfigurableKotlinCompilerOptions<KotlinMultiplatformExtension, KotlinCommonCompilerOptions> {

    val withSourcesJar: Boolean?

    context(project: Project)
    override fun applyTo(receiver: KotlinMultiplatformExtension) {
        super<KotlinBaseExtension>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        withSourcesJar?.let(project.kotlin::withSourcesJar)
    }

    context(project: Project)
    fun applyTo() = applyTo(project.kotlin)
}
