package gradle.plugins.kotlin.mpp

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.api.trySet
import gradle.ifTrue
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.targets.jvm.KotlinJvmCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

@Serializable
@SerialName("android")
internal data class KotlinAndroidTarget(
    override val name: String = "android",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<KotlinJvmAndroidCompilation>? = null,
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
    val setPublishLibraryVariants: List<String>? = null,
    /** Set up all of the Android library variants to be published from this target's project within the default publications, which are
     * set up if the `maven-publish` Gradle plugin is applied. This overrides the variants chosen with [publishLibraryVariants] */
    val publishAllLibraryVariants: Boolean? = null,
    /** If true, a publication will be created per merged product flavor, with the build types used as classifiers for the artifacts
     * published within each publication. If set to false, each Android variant will have a separate publication. */
    val publishLibraryVariantsGroupedByFlavor: Boolean? = null,
) : AbstractKotlinTarget<KotlinAndroidTarget>(),
    HasConfigurableKotlinCompilerOptions<KotlinAndroidTarget, org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions> {

    context(Project)
    override fun applyTo(receiver: KotlinAndroidTarget) {
        super<AbstractKotlinTarget>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        // Applicable only in library project.
        publishLibraryVariants?.toTypedArray()?.let(receiver::publishLibraryVariants)

        // Applicable only in library project.
        setPublishLibraryVariants?.let { setPublishLibraryVariants ->
            receiver.publishLibraryVariants = setPublishLibraryVariants
        }

        // Applicable only in library project.
        publishAllLibraryVariants?.ifTrue(receiver::publishAllLibraryVariants)
        receiver::publishLibraryVariantsGroupedByFlavor trySet publishLibraryVariantsGroupedByFlavor
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinAndroidTarget>()) { name, action ->
            project.kotlin.androidTarget(name, action::execute)
        }
}
