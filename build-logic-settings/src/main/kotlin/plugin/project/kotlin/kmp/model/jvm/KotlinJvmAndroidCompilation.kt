package plugin.project.kotlin.kmp.model.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.project.java.model.JavaCompile
import plugin.project.kotlin.kmp.model.KotlinSourceSet
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.kotlin.model.KotlinCompilationOutput
import plugin.project.kotlin.model.KotlinCompilationTask
import plugin.project.kotlin.model.configure

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
    override fun applyTo(compilations: NamedDomainObjectContainer<out org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>) {
        super.applyTo(compilations)

        compilations.configure {
            this as KotlinJvmAndroidCompilation

            this@KotlinJvmAndroidCompilation.compileTaskProvider?.let { compileTaskProvider ->
                compileTaskProvider {
                    compileTaskProvider.applyTo(this)
                }
            }

            this@KotlinJvmAndroidCompilation.compileJavaTaskProvider?.let { compileJavaTaskProvider ->
                compileJavaTaskProvider {
                    compileJavaTaskProvider.applyTo(this)
                }
            }
        }
    }
}
