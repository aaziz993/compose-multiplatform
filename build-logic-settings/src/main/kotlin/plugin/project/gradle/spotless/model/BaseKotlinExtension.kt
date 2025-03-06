package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.spotless.kotlin.KtfmtStep
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal abstract class BaseKotlinExtension : FormatExtension {

    abstract val diktat: DiktatConfig?

    /** Uses the <a href="https://github.com/facebookincubator/ktfmt">ktfmt</a> jar to format source code. */
    abstract val ktfmt: List<KtfmtConfig>?

    abstract val ktlint: KtlintConfig?

    context(Project)
    override fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
        super.applyTo(extension)

        extension as BaseKotlinExtension

        diktat?.let { diktat ->
            (diktat.version?.resolveVersion()?.let(extension::diktat) ?: extension.diktat()).apply {
                diktat.config?.let(::configFile)
            }
        }

        ktfmt?.forEach { ktfmt ->
            (ktfmt.version?.resolveVersion()?.let(extension::ktfmt) ?: extension.ktfmt()).apply {
                when (ktfmt.style) {
                    KtfmtStep.Style.DROPBOX -> dropboxStyle()
                    KtfmtStep.Style.GOOGLE -> googleStyle()
                    KtfmtStep.Style.KOTLINLANG -> kotlinlangStyle()
                    else -> throw IllegalArgumentException("Unsupported ktfmt default style")
                }.configure { options ->
                    ktfmt.options.maxWidth?.let(options::setMaxWidth)
                    ktfmt.options.blockIndent?.let(options::setBlockIndent)
                    ktfmt.options.continuationIndent?.let(options::setContinuationIndent)
                    ktfmt.options.removeUnusedImport?.let(options::setRemoveUnusedImport)
                }
            }
        }

        ktlint?.let { ktlint ->
            (ktlint.version?.resolveVersion()?.let(extension::ktlint) ?: extension.ktlint()).apply {
                ktlint.editorConfigPath?.let(::setEditorConfigPath)
                ktlint.editorConfigOverride?.let(::editorConfigOverride)
                ktlint.customRuleSets?.let(::customRuleSets)
            }
        }
    }
}
