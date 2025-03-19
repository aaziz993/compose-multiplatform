package gradle.plugins.kmp.jvm

import gradle.accessors.kotlin

import gradle.plugins.kmp.KotlinJvmAndAndroidTarget
import gradle.plugins.kmp.KotlinJvmAndroidCompilation
import gradle.plugins.kmp.KotlinJvmAndroidCompilationTransformingSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTarget(
    override val targetName: String = "jvm",
    override val compilations: List<@Serializable(with = KotlinJvmAndroidCompilationTransformingSerializer::class) KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    val testRuns: List<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDsl? = null,
    val withJava: Boolean? = null,
) : KotlinJvmAndAndroidTarget() {

        context(Project)
    override fun applyTo(named: T) {
        super.applyTo(named)

        named as KotlinJvmTarget

        testRuns?.forEach { testRun ->
            named.testRuns.named(testRun.name) {
                testRun.applyTo(this)
            }
        }

        mainRun?.let { mainRun ->
            named.mainRun(mainRun::applyTo)
        }

//        if (projectProperties.kotlin.targets.none { target -> target is KotlinAndroidTarget }) {
//            withJava?.takeIf { it }?.let { named.withJava() }
//        }
    }

    context(Project)
    override fun applyTo() =
        super<KotlinJvmAndAndroidTarget>.applyTo(kotlin.targets.withType<KotlinJvmTarget>(), kotlin::jvm)
}
