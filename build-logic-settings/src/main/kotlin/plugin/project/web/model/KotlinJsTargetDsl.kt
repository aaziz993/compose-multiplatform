package plugin.project.web.model

import plugin.project.kotlin.model.language.HasConfigurableKotlinCompilerOptions

internal interface KotlinJsTargetDsl : HasConfigurableKotlinCompilerOptions<KotlinJsCompilerOptions> {

    val moduleName: String?

    val useCommonJs: Boolean?

    val useEsModules: Boolean?

    /**
     * The function accepts [jsExpression] and puts this expression as the "args: Array<String>" argument in place of main-function call
     */
    val passAsArgumentToMainFunction: String?

    val generateTypeScriptDefinitions: Boolean?
}
