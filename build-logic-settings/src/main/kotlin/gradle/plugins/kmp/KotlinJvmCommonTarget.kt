package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kmp.jvm.KotlinJvmCompilerOptions
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

@Serializable
@SerialName("jvmCommon")
internal data class KotlinJvmCommonTarget(
    override val targetName: String? = null,
    override val compilations: Set<KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
) : KotlinTarget<org.jetbrains.kotlin.gradle.plugin.KotlinTarget>,
    HasConfigurableKotlinCompilerOptions<KotlinJvmTarget, org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions> {

    context(project: Project)
    override fun applyTo() {
        applyTo(project.kotlin.targets.withType<KotlinJvmTarget>()) { _, _ -> }
        applyTo(project.kotlin.targets.withType<KotlinAndroidTarget>()) { _, _ -> }
    }
}
