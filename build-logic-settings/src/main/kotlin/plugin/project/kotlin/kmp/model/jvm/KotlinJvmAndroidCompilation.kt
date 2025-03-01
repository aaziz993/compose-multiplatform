package plugin.project.kotlin.kmp.model.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.project.java.model.JavaCompile
import plugin.project.kotlin.kmp.model.KotlinSourceSet
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.kotlin.model.KotlinCompilationOutput
import plugin.project.kotlin.model.KotlinCompilationTask

@Serializable
internal data class KotlinJvmAndroidCompilation(
    override val compilationName: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = ProjectDependencyTransformingSerializer::class) ProjectDependency>? = null,
    val compileTaskProvider: KotlinCompilationTask<KotlinJvmCompilerOptions>? = null,
    val compileJavaTaskProvider: JavaCompile? = null,
) : KotlinCompilation {

    context(Project)
    fun applyTo(compilation: KotlinJvmAndroidCompilation) {
        compileTaskProvider?.let { compileTaskProvider ->
            compilation.compileTaskProvider.configure {
                compileTaskProvider.applyTo(this)
                compileTaskProvider.compilerOptions?.applyTo(compilerOptions)
            }
        }

        compileJavaTaskProvider?.let { compileJavaTaskProvider ->
            compilation.compileJavaTaskProvider.configure {
                compileJavaTaskProvider.applyTo(this)
            }
        }
    }
}
