package gradle.plugins.kmp.jvm

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kmp.KotlinJvmAndroidCompilation
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.jvm.test.KotlinJvmTestRun
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTarget(
    override val name: String = "jvm",
    override val compilations: LinkedHashSet<@Serializable(with = KotlinJvmAndroidCompilationKeyTransformingSerializer::class) KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    val testRuns: List<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDsl? = null,
) : KotlinTarget<KotlinJvmTarget>,
    HasConfigurableKotlinCompilerOptions<KotlinJvmTarget, org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions> {

    context(Project)
    override fun applyTo(receiver: KotlinJvmTarget) {
        super<KotlinTarget>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        testRuns?.forEach { testRun ->
            testRun.applyTo(receiver.testRuns)
        }

        mainRun?.let { mainRun ->
            receiver.mainRun {
                mainRun.applyTo(this)
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinJvmTarget>()) { name, action ->
            project.kotlin.jvm(name, action::execute)
        }
}
