package plugin.project.kotlin.model.language.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import plugin.project.kotlin.model.language.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.language.KotlinTarget

internal  interface KotlinJvmTarget : KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {
    val testRuns: List<KotlinJvmTestRun>?
    val mainRun: KotlinJvmRunDsl?
    val withJava: Boolean?
    override val compilerOptions: KotlinJvmCompilerOptions?


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
