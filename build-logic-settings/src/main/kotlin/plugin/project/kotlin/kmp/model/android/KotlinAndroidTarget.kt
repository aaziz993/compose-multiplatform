package plugin.project.kotlin.kmp.model.android

import gradle.kotlin
import gradle.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.kmp.model.jvm.KotlinJvmCompilerOptions
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.configure

@Serializable
@SerialName("android")
internal data class KotlinAndroidTarget(
    override val targetName: String = "android",
    override val compilations: List<plugin.project.kotlin.kmp.model.jvm.KotlinJvmAndroidCompilation>? = null,
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

    context(Project)
    override fun applyTo() {
        val targets =
            if (targetName.isEmpty())
                kotlin.targets.withType<KotlinAndroidTarget>()
            else container { kotlin.androidTarget(targetName) }

        super<KotlinTarget>.applyTo(targets)

        targets.configure {
            this@KotlinAndroidTarget.compilerOptions?.applyTo(compilerOptions)

            this@KotlinAndroidTarget.publishLibraryVariants?.let { _publishLibraryVariants ->
                publishLibraryVariants = _publishLibraryVariants
            }

            this@KotlinAndroidTarget.publishAllLibraryVariants?.takeIf { it }?.run { publishAllLibraryVariants() }
            ::publishLibraryVariantsGroupedByFlavor trySet this@KotlinAndroidTarget.publishLibraryVariantsGroupedByFlavor
        }
    }
}
