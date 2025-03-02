package plugin.project.kotlin.kmp.model.jvm

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.configure

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
        val targets =
            if (targetName.isEmpty())
                kotlin.targets.withType<KotlinJvmTarget>()
            else container { kotlin.jvm(targetName) }

        super<KotlinTarget>.applyTo(targets)

        targets.configure {
            this@KotlinJvmTarget.compilerOptions?.applyTo(compilerOptions)

            this@KotlinJvmTarget.testRuns?.forEach { _testRuns ->
                _testRuns.applyTo(testRuns)
            }

            this@KotlinJvmTarget.mainRun?.let { mainRun ->
                mainRun(mainRun::applyTo)
            }

            this@KotlinJvmTarget.withJava?.takeIf { it }?.let { withJava() }
        }
    }
}
