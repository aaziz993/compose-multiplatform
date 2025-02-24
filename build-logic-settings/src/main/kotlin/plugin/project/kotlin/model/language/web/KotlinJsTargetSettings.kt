package plugin.project.kotlin.model.language.web

import kotlinx.serialization.Serializable
import plugin.project.model.EnabledSettings

@Serializable
internal data class KotlinJsTargetSettings(
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
    override val enabled: Boolean = true,
) : KotlinJsTargetDsl, EnabledSettings
