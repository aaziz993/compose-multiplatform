package plugin.project.kotlin.model.language.jvm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import plugin.project.model.EnabledSettings

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTargetSettings(
    override val testRuns: List<KotlinJvmTestRun>? = null,
    override val mainRun: KotlinJvmRunDsl? = null,
    override val withJava: Boolean? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val enabled: Boolean = true,
) : KotlinJvmTarget, EnabledSettings
