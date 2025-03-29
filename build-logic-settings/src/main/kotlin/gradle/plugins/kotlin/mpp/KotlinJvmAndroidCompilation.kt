package gradle.plugins.kotlin.mpp

import gradle.api.NamedObjectTransformingSerializer
import gradle.plugins.java.JavaCompile
import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.KotlinCompilationOutput

import gradle.plugins.kotlin.KotlinSourceSet
import gradle.plugins.kotlin.targets.jvm.KotlinJvmCompilerOptions
import gradle.plugins.kotlin.tasks.KotlinCompilationTaskImpl
import gradle.plugins.project.Dependency
import kotlinx.serialization.ExperimentalSerializationApi
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
    override val compileTaskProvider: KotlinCompilationTaskImpl? = null,
    val compileJavaTaskProvider: JavaCompile? = null,
) : KotlinCompilation<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation) {
        super.applyTo(receiver)

        compileTaskProvider?.let { compileTaskProvider ->
            receiver.compileTaskProvider {
                KotlinJvmCompilerOptions
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
