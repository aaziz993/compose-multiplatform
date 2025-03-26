package gradle.plugins.kmp.nat

import gradle.api.applyTo
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompile
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompileImpl
import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinCompilationTransformingSerializer
import gradle.plugins.project.Dependency
import gradle.plugins.project.DependencyKeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KotlinNativeCompilation(
    override val name: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val compileTaskProvider: KotlinNativeCompileImpl? = null,
    override val associatesWith: Set<String>? = null,
    override val dependencies: Set<@Serializable(with = DependencyKeyTransformingSerializer::class) Dependency>? = null,
    // Interop DSL.
    val cinterops: LinkedHashSet<@Serializable(with = DefaultCInteropSettingsKeyTransformingSerializer::class) DefaultCInteropSettings>? = null
) : AbstractKotlinNativeCompilation<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation>() {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation) {
        super.applyTo(receiver)

        cinterops?.forEach { cinterops ->
            cinterops.applyTo(receiver.cinterops)
        }
    }
}

internal object KotlinNativeCompilationKeyTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinNativeCompilation>(
        KotlinNativeCompilation.serializer(),
    )
