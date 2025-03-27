package gradle.plugins.kotlin.mpp

import gradle.accessors.id
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.hierarchy.KotlinHierarchyDsl
import gradle.plugins.kotlin.KotlinProjectExtension
import gradle.plugins.kotlin.KotlinTargetContainerWithJsPresetFunctions
import gradle.plugins.kotlin.KotlinTargetContainerWithPresetFunctions
import gradle.plugins.kotlin.KotlinTargetContainerWithWasmPresetFunctions
import gradle.plugins.kotlin.KotlinTargetsContainer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal abstract class KotlinMultiplatformExtension :
    KotlinProjectExtension<KotlinMultiplatformExtension>(),
    KotlinTargetsContainer<KotlinMultiplatformExtension>,
    KotlinTargetContainerWithPresetFunctions<KotlinMultiplatformExtension>,
    KotlinTargetContainerWithJsPresetFunctions<KotlinMultiplatformExtension>,
    KotlinTargetContainerWithWasmPresetFunctions<KotlinMultiplatformExtension>,
    KotlinHierarchyDsl<KotlinMultiplatformExtension>,
    HasConfigurableKotlinCompilerOptions<KotlinMultiplatformExtension, KotlinCommonCompilerOptions> {

    abstract val withSourcesJar: Boolean?

    context(Project)
    override fun applyTo(receiver: KotlinMultiplatformExtension) {
        super<KotlinProjectExtension>.applyTo(receiver)
        super<KotlinTargetsContainer>.applyTo(receiver)
        super<KotlinTargetContainerWithPresetFunctions>.applyTo(receiver)
        super<KotlinTargetContainerWithJsPresetFunctions>.applyTo(receiver)
        super<KotlinTargetContainerWithWasmPresetFunctions>.applyTo(receiver)
        super<KotlinHierarchyDsl>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        withSourcesJar?.let(project.kotlin::withSourcesJar)
    }

    context(Project)
    open fun applyTo() = project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("kotlin.multiplatform").id) {
        applyTo(project.kotlin)
    }
}
