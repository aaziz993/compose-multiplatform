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
    override fun applyTo(named: T) {
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
