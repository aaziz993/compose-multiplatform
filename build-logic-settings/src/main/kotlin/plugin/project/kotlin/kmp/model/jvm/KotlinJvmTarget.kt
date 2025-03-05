package plugin.project.kotlin.kmp.model.jvm

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTarget(
    override val targetName: String = "jvm",
    override val compilations: List<@Serializable(with = KotlinJvmAndroidCompilationTransformingSerializer::class) KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    val testRuns: List<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDsl? = null,
    val withJava: Boolean? = null,
) : KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    override val needKmp: Boolean
        get() = false

    context(Project)
    override fun applyTo() {
        val target = create(kotlin::jvm)

        super<KotlinTarget>.applyTo()

        if (target !is KotlinJvmTarget) return

        super<HasConfigurableKotlinCompilerOptions>.applyTo(target)

        testRuns?.forEach { testRun ->
            target.testRuns.named(testRun.name) {
                testRun.applyTo(this)
            }
        }

        mainRun?.let { mainRun ->
            target.mainRun(mainRun::applyTo)
        }

        withJava?.takeIf { it }?.let { target.withJava() }
    }
}
