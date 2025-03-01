package plugin.project.kotlin.kmp.model

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import plugin.project.android.model.BaseExtension
import plugin.project.java.model.JavaToolchainSpec
import plugin.project.java.model.application.JavaApplication
import plugin.project.kotlin.cocoapods.model.CocoapodsSettings
import plugin.project.kotlin.model.KotlinCommonCompilerOptionsImpl

@Serializable
internal data class KotlinMultiplatformSettings(
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
    val application: JavaApplication? = null,
    val android: BaseExtension? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : KotlinMultiplatformExtension {

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
