package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import plugin.project.model.Dependency
import plugin.project.model.DependencyTransformingSerializer
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.kotlin.model.KotlinCompilationTransformingSerializer

@Serializable
internal data class KotlinNativeCompilation(
    override val compilationName: String,
    override val defaultSourceSet: plugin.project.kotlin.kmp.model.KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: plugin.project.kotlin.model.KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    // Interop DSL.

    val cinterops: List<DefaultCInteropSettings>? = null
) : KotlinCompilation {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation

        cinterops?.forEach { cinterops ->
            named.cinterops.named(cinterops.name) {
                cinterops.applyTo(this)
            }
        }
    }
}

internal object KotlinNativeCompilationTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinNativeCompilation>(
        KotlinNativeCompilation.serializer(),
    )
