package gradle.plugins.kotlin

import gradle.accessors.id
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.kmp.HierarchyAliasTransformingSerializer
import gradle.plugins.kmp.HierarchyGroup
import gradle.plugins.kmp.KotlinMultiplatformExtension
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.kmp.KotlinSourceSetTransformingSerializer
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.KotlinTargetTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import plugins.kotlin.cocoapods.model.CocoapodsSettings

@Serializable
internal data class KotlinSettings(
    override val withSourcesJar: Boolean? = null,
    override val jvmToolchainSpec: JavaToolchainSpec? = null,
    override val jvmToolchain: Int? = null,
    override val kotlinDaemonJvmArgs: List<String>? = null,
    override val compilerVersion: String? = null,
    override val coreLibrariesVersion: String? = null,
    override val explicitApi: ExplicitApiMode? = null,
    override val compilerOptions: KotlinCommonCompilerOptionsImpl? = null,
    val targets: List<@Serializable(with = KotlinTargetTransformingSerializer::class) KotlinTarget> = emptyList(),
    val hierarchy: List<@Serializable(with = HierarchyAliasTransformingSerializer::class) HierarchyGroup> = emptyList(),
    val sourceSets: List<@Serializable(with = KotlinSourceSetTransformingSerializer::class) KotlinSourceSet> = emptyList(),
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : KotlinMultiplatformExtension {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlin.multiplatform").id) {
            super.applyTo(kotlin as KotlinBaseExtension)

            targets.forEach { target -> target.applyTo() }

            kotlin.applyDefaultHierarchyTemplate {
                common {
                    hierarchy.forEach { hierarchy ->
                        hierarchy.applyTo(this)
                    }
                }
            }

            sourceSets.forEach { sourceSet ->
                sourceSet.applyTo()
            }
        }
}
