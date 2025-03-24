package gradle.plugins.kmp.jvm

import gradle.accessors.kotlin
import gradle.api.applyTo

import gradle.plugins.kmp.KotlinJvmAndAndroidTarget
import gradle.plugins.kmp.KotlinJvmAndroidCompilation
import gradle.plugins.kmp.KotlinJvmAndroidCompilationTransformingSerializer
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
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
    override val compilations: Set<@Serializable(with = KotlinJvmAndroidCompilationTransformingSerializer::class) KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    val testRuns: List<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDsl? = null,
    val withJava: Boolean? = null,
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
            receiver.mainRun(mainRun::applyTo)
        }

//        if (projectProperties.kotlin.targets.none { target -> target is KotlinAndroidTarget }) {
//            withJava?.takeIf { it }?.let { named.withJava() }
//        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(kotlin.targets.withType<KotlinJvmTarget>(), kotlin::jvm)
}
