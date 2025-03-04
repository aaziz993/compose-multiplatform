package plugin.project.kotlin.kmp.model.jvm

import gradle.containerize
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.project.java.model.JavaCompile
import plugin.project.kotlin.kmp.model.KotlinSourceSet
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.kotlin.model.KotlinCompilationOutput
import plugin.project.kotlin.model.KotlinCompilationTask
import plugin.project.kotlin.model.KotlinCompilationTransformingSerializer

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
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation

        compileTaskProvider?.let { compileTaskProvider ->
            named.compileTaskProvider {
                compileTaskProvider.applyTo(this)
            }
        }

        compileJavaTaskProvider?.let { compileJavaTaskProvider ->
            named.compileJavaTaskProvider {
                compileJavaTaskProvider.applyTo(this as Named)
            }
        }
    }
}

internal object KotlinJvmAndroidCompilationTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinJvmAndroidCompilation>(
            KotlinJvmAndroidCompilation.serializer(),
    )
