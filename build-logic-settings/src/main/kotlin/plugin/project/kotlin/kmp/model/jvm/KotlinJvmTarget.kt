package plugin.project.kotlin.kmp.model.jvm

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTarget(
    override val targetName: String = "",
    override val compilations: List<KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    val testRuns: List<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDsl? = null,
    val withJava: Boolean? = null,
) : KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    context(Project)
    override fun applyTo() {
        val target = targetName.takeIf(String::isNotEmpty)?.let(kotlin::jvm) ?: kotlin.jvm()

        super<KotlinTarget>.applyTo(target)

        compilerOptions?.applyTo(target.compilerOptions)

        testRuns?.forEach { testRuns ->
            testRuns.applyTo(target.testRuns)
        }

        mainRun?.let { mainRun ->
            target.mainRun(mainRun::applyTo)
        }

        withJava?.takeIf { it }?.let { target.withJava() }
    }
}
