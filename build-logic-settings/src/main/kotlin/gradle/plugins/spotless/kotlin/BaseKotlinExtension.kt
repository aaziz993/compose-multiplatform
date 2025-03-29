package gradle.plugins.spotless.kotlin

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.spotless.kotlin.DiktatStep
import com.diffplug.spotless.kotlin.KtLintStep
import com.diffplug.spotless.kotlin.KtfmtStep
import gradle.accessors.catalog.libs
import gradle.accessors.settings

import gradle.api.trySet
import gradle.plugins.spotless.FormatExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class BaseKotlinExtension<T : BaseKotlinExtension> : FormatExtension<T>() {

    abstract val diktat: DiktatConfig?

    /** Uses the <a href="https://github.com/facebookincubator/ktfmt">ktfmt</a> jar to format source code. */
    abstract val ktfmt: List<KtfmtConfig>?

    abstract val ktlint: KtlintConfig?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        diktat?.let { diktat ->
            diktat.applyTo(
                receiver.diktat(
                    diktat.version ?: project.settings.libs.versionOrNull("diktat")
                    ?: DiktatStep.defaultVersionDiktat(),
                ),
            )
        }

        ktfmt?.forEach { ktfmt ->
            ktfmt.applyTo(
                receiver.ktfmt(
                    ktfmt.version ?: project.settings.libs.versionOrNull("ktfmt")
                    ?: KtfmtStep.defaultVersion(),
                ),
            )
        }

        ktlint?.let { ktlint ->
            ktlint.applyTo(
                receiver.ktlint(
                    ktlint.version ?: project.settings.libs.versionOrNull("ktlint")
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

        fun applyTo(receiver: BaseKotlinExtension.DiktatConfig) {
            receiver::configFile trySet configFile
        }
    }

    @Serializable
    internal data class KtfmtConfig(
        val version: String? = null,
        val style: KtfmtStep.Style,
        val options: KtfmtFormattingOptions
    ) {

        fun applyTo(receiver: BaseKotlinExtension.KtfmtConfig) =
            when (style) {
                KtfmtStep.Style.DROPBOX -> receiver.dropboxStyle()
                KtfmtStep.Style.GOOGLE -> receiver.googleStyle()
                KtfmtStep.Style.KOTLINLANG -> receiver.kotlinlangStyle()
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

        fun applyTo(receiver: KtfmtStep.KtfmtFormattingOptions) {
            receiver::setMaxWidth trySet maxWidth
            receiver::setBlockIndent trySet blockIndent
            receiver::setContinuationIndent trySet continuationIndent
            receiver::setRemoveUnusedImport trySet removeUnusedImport
        }
    }

    @Serializable
    internal data class KtlintConfig(
        val version: String? = null,
        val editorConfigPath: String? = null,
        val editorConfigOverride: Map<String, String>? = null,
        val customRuleSets: List<String>? = null,
    ) {

        fun applyTo(receiver: BaseKotlinExtension.KtlintConfig) {
            receiver::setEditorConfigPath trySet editorConfigPath
            receiver::editorConfigOverride trySet editorConfigOverride
            receiver::customRuleSets trySet customRuleSets
        }
    }
}
