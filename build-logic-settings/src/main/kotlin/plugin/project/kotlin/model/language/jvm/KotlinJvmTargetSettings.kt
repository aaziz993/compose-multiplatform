package plugin.project.kotlin.model.language.jvm

import kotlinx.serialization.Serializable
import plugin.project.model.EnabledSettings

@Serializable
internal data class KotlinJvmTargetSettings(
    override val testRuns: List<KotlinJvmTestRun>? = null,
    override val mainRun: KotlinJvmRunDsl? = null,
    override var withJava: Boolean? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val enabled: Boolean = true,
) : KotlinJvmTarget, EnabledSettings
