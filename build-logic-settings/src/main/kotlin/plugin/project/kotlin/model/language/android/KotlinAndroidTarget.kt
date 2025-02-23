package plugin.project.kotlin.model.language.android

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import plugin.project.kotlin.model.language.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.language.KotlinTarget
import plugin.project.kotlin.model.language.jvm.KotlinJvmCompilerOptions

@Serializable
internal data class KotlinAndroidTarget(
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
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
) : KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    fun applyTo(target: KotlinAndroidTarget) {
        publishLibraryVariants?.let { publishLibraryVariants ->
            target.publishLibraryVariants = publishLibraryVariants
        }
        publishAllLibraryVariants?.takeIf { it }?.run { target.publishAllLibraryVariants() }
        target::publishLibraryVariantsGroupedByFlavor trySet publishLibraryVariantsGroupedByFlavor
        compilerOptions?.applyTo(target.compilerOptions)
    }
}
