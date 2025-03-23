package gradle.plugins.kmp.nat

import gradle.api.applyTo
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinCompilationTransformingSerializer
import gradle.project.Dependency
import gradle.project.DependencyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

@Serializable
internal data class KotlinNativeCompilation(
    override val compilationName: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    // Interop DSL.
    val cinterops: List<DefaultCInteropSettings>? = null
) : KotlinCompilation<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation) {
        super.applyTo(receiver)

        cinterops?.forEach { cinterops ->
            cinterops.applyTo(receiver.cinterops)
        }
    }
}

internal object KotlinNativeCompilationTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinNativeCompilation>(
        KotlinNativeCompilation.serializer(),
    )
