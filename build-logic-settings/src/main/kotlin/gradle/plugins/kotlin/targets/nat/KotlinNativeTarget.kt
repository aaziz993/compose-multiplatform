package gradle.plugins.kotlin.targets.nat

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.mpp.KotlinTargetWithBinaries
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinNativeTarget<T : org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>
    : KotlinTarget<T>,
    HasConfigurableKotlinCompilerOptions<T, org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompilerOptions>,
    KotlinTargetWithBinaries<
        T,
        org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation,
        org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer,
        KotlinNativeBinaryContainer,
        >() {

    abstract override val compilations: LinkedHashSet<KotlinNativeCompilation>?

    context(Project)
    override fun applyTo(receiver: T) {
        super<KotlinTarget>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)
    }
}

@Serializable
@SerialName("native")
internal data class KotlinNativeTargetImpl(
    override val name: String? = null,
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()) { _, _ -> }
}
