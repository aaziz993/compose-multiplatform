package gradle.plugins.kmp

import gradle.plugins.java.JavaCompile
import gradle.plugins.kmp.jvm.KotlinJvmCompilerOptions
import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinCompilationTransformingSerializer
import gradle.plugins.kotlin.tasks.KotlinCompilationTaskImpl
import gradle.project.Dependency
import gradle.project.DependencyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

@Serializable
internal data class KotlinJvmAndroidCompilation(
    override val compilationName: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    val compileTaskProvider: KotlinCompilationTaskImpl? = null,
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

internal object KotlinJvmAndroidCompilationTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinJvmAndroidCompilation>(
        KotlinJvmAndroidCompilation.serializer(),
    )
