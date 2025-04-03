package gradle.plugins.kotlin.targets.jvm

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinJvmCompilerOptions
import gradle.plugins.kotlin.KotlinTargetWithTests
import gradle.plugins.kotlin.mpp.KotlinJvmAndroidCompilation
import gradle.plugins.kotlin.mpp.KotlinOnlyTarget
import gradle.plugins.kotlin.targets.jvm.test.KotlinJvmTestRun
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
    override val compilations: LinkedHashSet<KotlinJvmAndroidCompilation>? = null,
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
