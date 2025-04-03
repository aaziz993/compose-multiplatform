package gradle.plugins.kotlin.mpp

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.artifacts.Dependency
import gradle.plugins.java.JavaCompile
import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinJvmCompilerOptions
import gradle.plugins.kotlin.KotlinSourceSet
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

@KeepGeneratedSerializer
@Serializable(with = KotlinJvmAndroidCompilationObjectTransformingSerializer::class)
internal data class KotlinJvmAndroidCompilation(
    override val name: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: Set<Dependency>? = null,
    override val compileTaskProvider: KotlinJvmAndAndroidCompilationTask? = null,
    val compileJavaTaskProvider: JavaCompile? = null,
) : KotlinCompilation<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation) {
        super.applyTo(receiver)

        compileTaskProvider?.let { compileTaskProvider ->
            receiver.compileTaskProvider {
                compileTaskProvider.applyTo(this)
            }
        }

        compileJavaTaskProvider?.let { compileJavaTaskProvider ->
            receiver.compileJavaTaskProvider {
                compileJavaTaskProvider.applyTo(this)
            }
        }
    }
}

private object KotlinJvmAndroidCompilationObjectTransformingSerializer :
    NamedObjectTransformingSerializer<KotlinJvmAndroidCompilation>(
        KotlinJvmAndroidCompilation.generatedSerializer(),
    )

@Serializable
internal data class KotlinJvmAndAndroidCompilationTask(
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
) : KotlinCompilationTask<
    org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions>,
    org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions,
    > {

    context(Project@Project)
    override fun applyTo() = throw UnsupportedOperationException("Not implemented")
}
