package plugin.project.kotlin.model.language.jvm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import plugin.project.kotlin.model.language.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.language.KotlinTarget

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTarget(
    override val name: String? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    val testRuns: List<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDsl? = null,
    val withJava: Boolean? = null,
) : KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    context(Project)
    fun applyTo(target: KotlinJvmTarget) {
        target.targetName
        testRuns?.forEach { testRuns ->
            testRuns.name.takeIf(String::isNotEmpty)?.also { name ->
                target.testRuns.named(name) {
                    testRuns.applyTo(this)
                }
            } ?: target.testRuns.all {
                testRuns.applyTo(this)
            }
        }

        mainRun?.let { mainRun ->
            target.mainRun(mainRun::applyTo)
        }

        withJava?.takeIf { it }?.let { target.withJava() }
        compilerOptions?.applyTo(target.compilerOptions)
    }
}
