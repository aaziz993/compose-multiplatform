package gradle.model.kotlin.kmp.nat

import gradle.model.kotlin.KotlinCompilation
import gradle.model.kotlin.KotlinCompilationOutput
import gradle.model.kotlin.KotlinCompilationTransformingSerializer
import gradle.model.kotlin.kmp.KotlinSourceSet
import gradle.model.project.Dependency
import gradle.model.project.DependencyTransformingSerializer
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
