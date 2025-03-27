package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinBaseExtension
import gradle.plugins.kotlin.KotlinHierarchyDsl
import gradle.plugins.kotlin.KotlinProjectExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension

internal abstract class KotlinMultiplatformExtension :
    KotlinProjectExtension<KotlinMultiplatformExtension>(),
    KotlinHierarchyDsl<KotlinMultiplatformExtension>,
    HasConfigurableKotlinCompilerOptions<KotlinMultiplatformExtension, KotlinCommonCompilerOptions> {

    abstract val withSourcesJar: Boolean?

    context(Project)
    override fun applyTo(receiver: KotlinMultiplatformExtension) {
        super<KotlinProjectExtension>.applyTo(receiver)
        super<KotlinHierarchyDsl>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        withSourcesJar?.let(project.kotlin::withSourcesJar)
    }

    context(Project)
    fun applyTo() = applyTo(project.kotlin)
}
