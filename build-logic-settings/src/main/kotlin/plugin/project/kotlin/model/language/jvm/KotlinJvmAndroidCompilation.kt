package plugin.project.kotlin.model.language.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation
import plugin.model.dependency.ProjectDependency
import plugin.project.kotlin.model.language.KotlinCompilation
import plugin.project.kotlin.model.language.KotlinCompilationOutput
import plugin.project.kotlin.model.language.KotlinCompilationTask
import plugin.project.kotlin.model.language.KotlinSourceSet

@Serializable
internal data class KotlinJvmAndroidCompilation(
    override val compilationName: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<ProjectDependency>? = null,
    val compileTaskProvider: KotlinCompilationTask<KotlinJvmCompilerOptions>? = null,
    val compileJavaTaskProvider: JavaCompile? = null,
) : KotlinCompilation {

    context(Project)
     fun applyTo(compilation: KotlinJvmAndroidCompilation) {
         compileTaskProvider?.let{compileTaskProvider->
             compilation.compileTaskProvider
         }

        compilation.compileJavaTaskProvider.configure {
            this.
        }
    }
}
