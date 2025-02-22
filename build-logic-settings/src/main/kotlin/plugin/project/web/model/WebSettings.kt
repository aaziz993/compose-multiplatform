package plugin.project.web.model

import gradle.projectProperties
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import plugin.project.web.js.karakum.model.KarakumSettings
import plugin.project.web.node.model.NodeSettings
import plugin.project.web.npm.model.NpmSettings
import plugin.project.web.yarn.model.YarnSettings

@Serializable
internal data class WebSettings(
    override val moduleName: String? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    val browser: BrowserSettings = BrowserSettings(),
    val node: NodeSettings = NodeSettings(),
    val yarn: YarnSettings = YarnSettings(),
    val npm: NpmSettings = NpmSettings(),
    val karakum: KarakumSettings = KarakumSettings(),
) : KotlinJsTargetDsl {

    context(Project)
    @OptIn(ExperimentalDistributionDsl::class)
    override fun applyTo(target: org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl) {
        super.applyTo(target)

        if (browser.enabled) {
            target.browser {
                browser.applyTo(this)
            }
        }

        if (node.enabled) {
            target.nodejs()
        }

        if (settings.projectProperties.application) {
            target.binaries.executable()
        }
        else {
            target.binaries.library()
        }
    }
}
