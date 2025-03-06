package plugin.project.kotlin.kmp.model.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import plugin.project.model.Dependency
import plugin.project.model.DependencyTransformingSerializer
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
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
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
