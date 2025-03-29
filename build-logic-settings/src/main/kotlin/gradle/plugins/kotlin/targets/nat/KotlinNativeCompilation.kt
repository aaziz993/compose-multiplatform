package gradle.plugins.kotlin.targets.nat

import gradle.api.NamedKeyValueTransformingSerializer
import gradle.api.applyTo
import gradle.plugins.kotlin.KotlinCompilationOutput

import gradle.plugins.kotlin.KotlinSourceSet
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompileImpl
import gradle.plugins.project.Dependency
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer

import kotlinx.serialization.Serializable
import org.gradle.api.Project

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = KotlinNativeCompilationKeyValueTransformingSerializer::class)
internal data class KotlinNativeCompilation(
    override val name: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val compileTaskProvider: KotlinNativeCompileImpl? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: Set<Dependency>? = null,
    // Interop DSL.
    val cinterops: LinkedHashSet<DefaultCInteropSettings>? = null
) : AbstractKotlinNativeCompilation<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation>() {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation) {
        super.applyTo(receiver)

        cinterops?.forEach { cinterops ->
            cinterops.applyTo(receiver.cinterops)
        }
    }
}

private object KotlinNativeCompilationKeyValueTransformingSerializer :
    NamedKeyValueTransformingSerializer<KotlinNativeCompilation>(
        KotlinNativeCompilation.generatedSerializer(),
    )
