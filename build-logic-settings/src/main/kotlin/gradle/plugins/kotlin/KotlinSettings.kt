package gradle.plugins.kotlin

import gradle.accessors.id
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.kmp.HierarchyAliasKeyTransformingSerializer
import gradle.plugins.kmp.HierarchyGroup
import gradle.plugins.kmp.KotlinMultiplatformExtension
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.kmp.KotlinSourceSetKeyTransformingSerializer
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.KotlinTargetKeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import gradle.plugins.kotlin.cocoapods.model.CocoapodsSettings

@Serializable
internal data class KotlinSettings(
    override val withSourcesJar: Boolean? = null,
    override val jvmToolchainSpec: JavaToolchainSpec? = null,
    override val jvmToolchain: Int? = null,
    override val kotlinDaemonJvmArgs: List<String>? = null,
    override val setKotlinDaemonJvmArgs: List<String>? = null,
    override val compilerVersion: String? = null,
    override val coreLibrariesVersion: String? = null,
    override val explicitApi: ExplicitApiMode? = null,
    override val compilerOptions: KotlinCommonCompilerOptionsImpl? = null,
    val targets: LinkedHashSet<@Serializable(with = KotlinTargetKeyTransformingSerializer::class) KotlinTarget<*>> = linkedSetOf(),
    val hierarchy: Set<@Serializable(with = HierarchyAliasKeyTransformingSerializer::class) HierarchyGroup> = emptySet(),
    val sourceSets: LinkedHashSet<@Serializable(with = KotlinSourceSetKeyTransformingSerializer::class) KotlinSourceSet> = linkedSetOf(),
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : KotlinMultiplatformExtension {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("kotlin.multiplatform").id) {
            super.applyTo()

            targets.forEach { target -> target.applyTo() }

            project.kotlin.applyDefaultHierarchyTemplate {
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
