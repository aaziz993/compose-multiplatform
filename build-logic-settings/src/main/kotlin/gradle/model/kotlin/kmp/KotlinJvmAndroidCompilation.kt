package gradle.model.kotlin.kmp

import gradle.model.java.JavaCompile
import gradle.model.kotlin.KotlinCompilation
import gradle.model.kotlin.KotlinCompilationOutput
import gradle.model.kotlin.KotlinCompilationTaskImpl
import gradle.model.kotlin.KotlinCompilationTransformingSerializer
import gradle.model.kotlin.kmp.jvm.KotlinJvmCompilerOptions
import gradle.model.project.Dependency
import gradle.model.project.DependencyTransformingSerializer
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
    val compileTaskProvider: KotlinCompilationTaskImpl<KotlinJvmCompilerOptions>? = null,
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
