package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinHierarchyDsl
import gradle.plugins.kotlin.KotlinProjectExtension
import gradle.plugins.kotlin.KotlinTargetsContainer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal abstract class KotlinMultiplatformExtension :
    KotlinProjectExtension<KotlinMultiplatformExtension>(),
    KotlinTargetsContainer<KotlinMultiplatformExtension>,
    KotlinHierarchyDsl<KotlinMultiplatformExtension>,
    HasConfigurableKotlinCompilerOptions<KotlinMultiplatformExtension, KotlinCommonCompilerOptions> {

    abstract val withSourcesJar: Boolean?

    context(Project)
    override fun applyTo(receiver: KotlinMultiplatformExtension) {
        super<KotlinProjectExtension>.applyTo(receiver)
        super<KotlinTargetsContainer>.applyTo(receiver)
        super<KotlinHierarchyDsl>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        withSourcesJar?.let(project.kotlin::withSourcesJar)
    }

    context(Project)
    fun applyTo() = applyTo(project.kotlin)
}
