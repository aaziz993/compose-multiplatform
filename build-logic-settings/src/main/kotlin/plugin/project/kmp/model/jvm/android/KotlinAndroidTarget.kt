package plugin.project.kmp.model.jvm.android

import gradle.kotlin
import gradle.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import plugin.project.kotlin.kmp.model.jvm.KotlinJvmAndAndroidTarget
import plugin.project.kotlin.kmp.model.jvm.KotlinJvmAndroidCompilation
import plugin.project.kotlin.kmp.model.jvm.KotlinJvmAndroidCompilationTransformingSerializer
import plugin.project.kotlin.kmp.model.jvm.KotlinJvmCompilerOptions

@Serializable
@SerialName("android")
internal data class KotlinAndroidTarget(
    override val targetName: String = "android",
    override val compilations: List<@Serializable(with = KotlinJvmAndroidCompilationTransformingSerializer::class) KotlinJvmAndroidCompilation>? = null,

    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    /** Names of the Android library variants that should be published from the target's project within the default publications which are
     * set up if the `maven-publish` Gradle plugin is applied.
     *
     * Item examples:
     * * 'release' (in case no product flavors were defined)
     * * 'fooRelease' (for the release build type of a flavor 'foo')
     * * 'fooBarRelease' (for the release build type multi-dimensional flavors 'foo' and 'bar').
     *
     * If set to null, which can also be done with [publishAllLibraryVariants],
     * all library variants will be published, but not test or application variants. */
    val publishLibraryVariants: List<String>? = null,
    /** Set up all of the Android library variants to be published from this target's project within the default publications, which are
     * set up if the `maven-publish` Gradle plugin is applied. This overrides the variants chosen with [publishLibraryVariants] */
    val publishAllLibraryVariants: Boolean? = null,
    /** If true, a publication will be created per merged product flavor, with the build types used as classifiers for the artifacts
     * published within each publication. If set to false, each Android variant will have a separate publication. */
    val publishLibraryVariantsGroupedByFlavor: Boolean? = null,
) : KotlinJvmAndAndroidTarget() {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinAndroidTarget

        publishLibraryVariants?.let { publishLibraryVariants ->
            named.publishLibraryVariants = publishLibraryVariants
        }

        publishAllLibraryVariants?.takeIf { it }?.run { named.publishAllLibraryVariants() }
        named::publishLibraryVariantsGroupedByFlavor trySet publishLibraryVariantsGroupedByFlavor
    }

    context(Project)
    override fun applyTo() {
        create(kotlin::androidTarget)

        super<KotlinJvmAndAndroidTarget>.applyTo(kotlin.targets.withType<KotlinAndroidTarget>())
    }
}
