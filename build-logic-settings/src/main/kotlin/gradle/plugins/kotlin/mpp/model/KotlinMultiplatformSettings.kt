package gradle.plugins.kotlin.mpp.model

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.kotlin.KotlinCommonCompilerOptionsImpl
import gradle.plugins.kotlin.KotlinSourceSet
import gradle.plugins.kotlin.KotlinSourceSetKeyTransformingSerializer
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.KotlinTargetKeyTransformingSerializer
import gradle.plugins.kotlin.cocoapods.model.CocoapodsSettings
import gradle.plugins.kotlin.hierarchy.KotlinHierarchyBuilder
import gradle.plugins.kotlin.mpp.KotlinAndroidTarget
import gradle.plugins.kotlin.mpp.KotlinMultiplatformExtension
import gradle.plugins.kotlin.targets.jvm.KotlinJvmTarget
import gradle.plugins.kotlin.targets.nat.KotlinNativeTargetImpl
import gradle.plugins.kotlin.targets.web.KotlinJsTarget
import gradle.plugins.kotlin.targets.web.KotlinWasmJsTargetDsl
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

@Serializable
internal data class KotlinMultiplatformSettings(
    override val withSourcesJar: Boolean? = null,
    override val jvmToolchainSpec: JavaToolchainSpec? = null,
    override val jvmToolchain: Int? = null,
    override val kotlinDaemonJvmArgs: List<String>? = null,
    override val setKotlinDaemonJvmArgs: List<String>? = null,
    override val compilerVersion: String? = null,
    override val coreLibrariesVersion: String? = null,
    override val explicitApi: ExplicitApiMode? = null,
    override val compilerOptions: KotlinCommonCompilerOptionsImpl? = null,
    override val sourceSets: LinkedHashSet<@Serializable(with = KotlinSourceSetKeyTransformingSerializer::class) KotlinSourceSet> = linkedSetOf(),
    override val targets: LinkedHashSet<@Serializable(with = KotlinTargetKeyTransformingSerializer::class) KotlinTarget<*>> = linkedSetOf(),
    override val applyHierarchyTemplate: KotlinHierarchyBuilder.Root? = null,
    val cocoapods: CocoapodsSettings = CocoapodsSettings(),
) : KotlinMultiplatformExtension() {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("kotlin.multiplatform").id) {
            super.applyTo()
        }
}
