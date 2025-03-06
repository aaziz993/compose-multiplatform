package gradle.model.kotlin

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import gradle.model.java.JavaToolchainSpec
import plugin.project.kotlin.cocoapods.model.CocoapodsSettings
import gradle.model.kmp.HierarchyAliasTransformingSerializer
import gradle.model.kmp.HierarchyGroup
import gradle.model.kmp.KotlinMultiplatformExtension
import gradle.model.kmp.KotlinSourceSet
import gradle.model.kmp.KotlinSourceSetTransformingSerializer
import gradle.model.kmp.KotlinTarget
import gradle.model.kmp.KotlinTargetTransformingSerializer

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
    val targets: List<@Serializable(with = KotlinTargetTransformingSerializer::class) KotlinTarget>? = null,
    val hierarchy: List<@Serializable(with = HierarchyAliasTransformingSerializer::class) HierarchyGroup>? = null,
    val sourceSets: List<@Serializable(with = KotlinSourceSetTransformingSerializer::class) KotlinSourceSet>? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : KotlinMultiplatformExtension {

    val enabledKMP: Boolean by lazy {
        targets?.filter(KotlinTarget::isLeaf)?.let { targets ->
            targets.any(KotlinTarget::needKMP) || targets.any { target -> target::class != targets.first()::class }
        } == true
    }

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlin.multiplatform").id) {
            super.applyTo(kotlin)

            targets?.forEach { target -> target.applyTo() }

            kotlin.applyDefaultHierarchyTemplate {
                common {
                    hierarchy?.forEach { hierarchy ->
                        hierarchy.applyTo(this)
                    }
                }
            }

            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo()
            }
        }
}

internal inline fun <reified T : KotlinTarget> KotlinSettings.sourceSets(): List<KotlinSourceSet>? {
    val _targets = targets?.filterIsInstance<T>() ?: return null

    return sourceSets?.filter { sourceSet ->
        sourceSet.name.isEmpty() || _targets.any { target -> sourceSet.name.startsWith(target.targetName) }
    }
}
