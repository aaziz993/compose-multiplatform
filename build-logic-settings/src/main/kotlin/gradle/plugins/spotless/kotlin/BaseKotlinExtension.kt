package gradle.plugins.spotless.kotlin

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.spotless.kotlin.DiktatStep
import com.diffplug.spotless.kotlin.KtLintStep
import com.diffplug.spotless.kotlin.KtfmtStep
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.plugins.spotless.FormatExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class BaseKotlinExtension<T : BaseKotlinExtension> : FormatExtension<T>() {

    abstract val diktat: DiktatConfig?

    /** Uses the <a href="https://github.com/facebookincubator/ktfmt">ktfmt</a> jar to format source code. */
    abstract val ktfmt: List<KtfmtConfig>?

    abstract val ktlint: KtlintConfig?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        diktat?.let { diktat ->
            diktat.applyTo(
                recipient.diktat(
                    diktat.version ?: settings.libs.versions.version("diktat")
                    ?: DiktatStep.defaultVersionDiktat(),
                ),
            )
        }

        ktfmt?.forEach { ktfmt ->
            ktfmt.applyTo(
                recipient.ktfmt(
                    ktfmt.version ?: settings.libs.versions.version("ktfmt")
                    ?: KtfmtStep.defaultVersion(),
                ),
            )
        }

        ktlint?.let { ktlint ->
            ktlint.applyTo(
                recipient.ktlint(
                    ktlint.version ?: settings.libs.versions.version("ktlint")
                    ?: KtLintStep.defaultVersion(),
                ),
            )
        }
    }

    @Serializable
    internal data class DiktatConfig(
        val version: String? = null,
        val configFile: String? = null
    ) {

        fun applyTo(recipient: BaseKotlinExtension.DiktatConfig) {
            configFile?.let(recipient::configFile)
        }
    }

    @Serializable
    internal data class KtfmtConfig(
        val version: String? = null,
        val style: KtfmtStep.Style,
        val options: KtfmtFormattingOptions
    ) {

        fun applyTo(recipient: BaseKotlinExtension.KtfmtConfig) =
            when (style) {
                KtfmtStep.Style.DROPBOX -> recipient.dropboxStyle()
                KtfmtStep.Style.GOOGLE -> recipient.googleStyle()
                KtfmtStep.Style.KOTLINLANG -> recipient.kotlinlangStyle()
                else -> throw IllegalArgumentException("Unsupported ktfmt default style")
            }.configure(options::applyTo)
    }

    @Serializable
    internal data class KtfmtFormattingOptions(
        val maxWidth: Int? = null,
        val blockIndent: Int? = null,
        val continuationIndent: Int? = null,
        val removeUnusedImport: Boolean? = null,
    ) {

        fun applyTo(recipient: KtfmtStep.KtfmtFormattingOptions) {
            maxWidth?.let(recipient::setMaxWidth)
            blockIndent?.let(recipient::setBlockIndent)
            continuationIndent?.let(recipient::setContinuationIndent)
            removeUnusedImport?.let(recipient::setRemoveUnusedImport)
        }
    }

    @Serializable
    internal data class KtlintConfig(
        val version: String? = null,
        val editorConfigPath: String? = null,
        val editorConfigOverride: Map<String, String>? = null,
        val customRuleSets: List<String>? = null,
    ) {

        fun applyTo(recipient: BaseKotlinExtension.KtlintConfig) {
            editorConfigPath?.let(recipient::setEditorConfigPath)
            editorConfigOverride?.let(recipient::editorConfigOverride)
            customRuleSets?.let(recipient::customRuleSets)
        }
    }
}
