package plugin.project.kotlin.model.language.web

import gradle.projectProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.kotlin.model.language.HasBinaries
import plugin.project.kotlin.model.language.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.language.KotlinTarget
import plugin.project.model.ProjectType

internal interface KotlinJsTargetDsl : KotlinTarget, KotlinTargetWithNodeJsDsl, HasBinaries<KotlinJsBinaryContainer>, HasConfigurableKotlinCompilerOptions<KotlinJsCompilerOptions> {

    val moduleName: String?

    val browser: KotlinJsBrowserDsl?

    val useCommonJs: Boolean?

    val useEsModules: Boolean?

    /**
     * The function accepts [jsExpression] and puts this expression as the "args: Array<String>" argument in place of main-function call
     */
    val passAsArgumentToMainFunction: String?

    val generateTypeScriptDefinitions: Boolean?

    context(Project)
    @OptIn(ExperimentalMainFunctionArgumentsDsl::class)
    fun applyTo(target: KotlinJsTargetDsl) {
        super.applyTo(target)

        target.moduleName = moduleName ?: moduleName

        nodejs?.let { nodejs ->
            target.nodejs {
                nodejs.applyTo(this)
            }
        }

        browser?.let { browser ->
            target.browser {
                browser.applyTo(this)
            }
        }

        useCommonJs?.takeIf { it }?.run { target.useCommonJs() }
        useEsModules?.takeIf { it }?.run { target.useEsModules() }
        passAsArgumentToMainFunction?.let(target::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIf { it }?.let { target.generateTypeScriptDefinitions() }

        when (projectProperties.type) {
            ProjectType.APP ->
                binaries.executable.let { binaries ->
                    binaries.compilation?.let { compilation ->
                        target.binaries.executable(target.compilations.getByName(compilation))
                    } ?: target.binaries.executable()
                }

            ProjectType.LIB -> binaries.library.let { binaries ->
                binaries.compilation?.let { compilation ->
                    target.binaries.library(target.compilations.getByName(compilation))
                } ?: target.binaries.library()
            }

            else -> Unit
        }
    }
}
