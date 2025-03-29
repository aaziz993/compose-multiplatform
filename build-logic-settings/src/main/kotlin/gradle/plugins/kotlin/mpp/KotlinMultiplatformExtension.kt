package gradle.plugins.kotlin.mpp

import gradle.accessors.catalog.libs
import gradle.accessors.kotlin
import gradle.accessors.settings
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinProjectExtension
import gradle.plugins.kotlin.KotlinTargetsContainer
import gradle.plugins.kotlin.hierarchy.KotlinHierarchyDsl
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal abstract class KotlinMultiplatformExtension :
    KotlinProjectExtension<KotlinMultiplatformExtension>(),
    KotlinTargetsContainer,
    KotlinHierarchyDsl,
    HasConfigurableKotlinCompilerOptions<KotlinMultiplatformExtension, KotlinCommonCompilerOptions> {

    abstract val metadata: Metadata?

    abstract val withSourcesJar: Boolean?

    context(Project)
    override fun applyTo(receiver: KotlinMultiplatformExtension) {
        super<KotlinProjectExtension>.applyTo(receiver)
        super<KotlinTargetsContainer>.applyTo()
        super<KotlinHierarchyDsl>.applyTo()
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        metadata?.let { metadata ->
            receiver.metadata {
                metadata.applyTo(this)
            }
        }

        withSourcesJar?.let(project.kotlin::withSourcesJar)
    }

    context(Project)
    override fun applyTo() = project.pluginManager.withPlugin(project.settings.libs.plugin("kotlin.multiplatform").id) {
        applyTo(project.kotlin)
    }
}
