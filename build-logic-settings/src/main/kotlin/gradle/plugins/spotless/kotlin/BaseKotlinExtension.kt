package gradle.plugins.spotless.kotlin

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.spotless.kotlin.KtfmtStep
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.plugins.spotless.FormatExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class BaseKotlinExtension : FormatExtension() {

    abstract val diktat: DiktatConfig?

    /** Uses the <a href="https://github.com/facebookincubator/ktfmt">ktfmt</a> jar to format source code. */
    abstract val ktfmt: List<KtfmtConfig>?

    abstract val ktlint: KtlintConfig?

    context(Project)
    override fun applyTo(recipient: com.diffplug.gradle.spotless.FormatExtension) {
        super.applyTo(extension)

        extension as BaseKotlinExtension

        diktat?.let { diktat ->
            diktat.applyTo(
                (diktat.version?.resolveVersion() ?: settings.libs.versions.version("diktat"))
                    ?.let(extension::diktat) ?: extension.diktat(),
            )
        }

        ktfmt?.forEach { ktfmt ->
            ktfmt.applyTo(
                (ktfmt.version?.resolveVersion() ?: settings.libs.versions.version("ktfmt"))
                    ?.let(extension::ktfmt) ?: extension.ktfmt(),
            )
        }

        ktlint?.let { ktlint ->
            ktlint.applyTo(
                (ktlint.version?.resolveVersion() ?: settings.libs.versions.version("ktlint"))
                    ?.let(extension::ktlint) ?: extension.ktlint(),
            )
        }
    }

    @Serializable
    internal data class DiktatConfig(
        val version: String? = null,
        val configFile: String? = null
    ) {

        fun applyTo(recipient: BaseKotlinExtension.DiktatConfig) {
            configFile?.let(config::configFile)
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
                KtfmtStep.Style.DROPBOX -> config.dropboxStyle()
                KtfmtStep.Style.GOOGLE -> config.googleStyle()
                KtfmtStep.Style.KOTLINLANG -> config.kotlinlangStyle()
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
            maxWidth?.let(options::setMaxWidth)
            blockIndent?.let(options::setBlockIndent)
            continuationIndent?.let(options::setContinuationIndent)
            removeUnusedImport?.let(options::setRemoveUnusedImport)
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
            editorConfigPath?.let(config::setEditorConfigPath)
            editorConfigOverride?.let(config::editorConfigOverride)
            customRuleSets?.let(config::customRuleSets)
        }
    }
}
