package gradle.plugins.kmp.jvm

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kmp.KotlinJvmAndroidCompilation
import gradle.plugins.kmp.KotlinJvmAndroidCompilationKeyTransformingSerializer
import gradle.plugins.kmp.KotlinOnlyTarget
import gradle.plugins.kmp.KotlinTargetWithTests
import gradle.plugins.kmp.jvm.test.KotlinJvmTestRun
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.JvmClasspathTestRunSource
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTarget(
    override val name: String = "jvm",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinJvmAndroidCompilationKeyTransformingSerializer::class) KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val testRuns: LinkedHashSet<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDslImpl? = null,
    val binaries: KotlinJvmBinariesDsl? = null,
) : KotlinOnlyTarget<KotlinJvmTarget, KotlinJvmCompilation>(),
    HasConfigurableKotlinCompilerOptions<KotlinJvmTarget, org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions>,
    KotlinTargetWithTests<KotlinJvmTarget, JvmClasspathTestRunSource, org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun> {

    context(Project)
    override fun applyTo(receiver: KotlinJvmTarget) {
        super<KotlinOnlyTarget>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        testRuns?.forEach { testRun ->
            testRun.applyTo(receiver.testRuns)
        }

        mainRun?.let { mainRun ->
            receiver.mainRun {
                mainRun.applyTo(this, receiver)
            }
        }

        binaries?.let { binaries ->
            receiver.binaries {
                binaries.applyTo(this)
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinJvmTarget>()) { name, action ->
            project.kotlin.jvm(name, action::execute)
        }
}
