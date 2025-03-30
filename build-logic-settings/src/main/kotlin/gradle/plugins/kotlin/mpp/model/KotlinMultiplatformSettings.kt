package gradle.plugins.kotlin.mpp.model

import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.kotlin.KotlinCommonCompilerOptionsImpl
import gradle.plugins.kotlin.KotlinSourceSet
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.cocoapods.model.CocoapodsSettings
import gradle.plugins.kotlin.hierarchy.KotlinHierarchyBuilder
import gradle.plugins.kotlin.mpp.KotlinMultiplatformExtension
import gradle.plugins.kotlin.mpp.Metadata
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

@Serializable
internal data class KotlinMultiplatformSettings(
    override val metadata: Metadata? = null,
    override val withSourcesJar: Boolean? = null,
    override val jvmToolchainSpec: JavaToolchainSpec? = null,
    override val jvmToolchain: Int? = null,
    override val kotlinDaemonJvmArgs: List<String>? = null,
    override val setKotlinDaemonJvmArgs: List<String>? = null,
    override val compilerVersion: String? = null,
    override val coreLibrariesVersion: String? = null,
    override val explicitApi: ExplicitApiMode? = null,
    override val compilerOptions: KotlinCommonCompilerOptionsImpl? = null,
    override val sourceSets: LinkedHashSet<KotlinSourceSet>? = null,
    override val targets: LinkedHashSet<KotlinTarget<*>>? = null,
    override val applyHierarchyTemplate: KotlinHierarchyBuilder.Root? = null,
    val cocoapods: CocoapodsSettings? = null,
) : KotlinMultiplatformExtension() {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            super.applyTo()
        }
}
