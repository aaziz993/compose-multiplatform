package gradle.model.gradle.spotless

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.spotless.kotlin.KtfmtStep
import kotlinx.serialization.Serializable

@Serializable
internal data class KtfmtConfig(
    val version: String? = null,
    val style: KtfmtStep.Style,
    val options: KtfmtFormattingOptions
) {

    fun applyTo(config: BaseKotlinExtension.KtfmtConfig) =
        when (style) {
            KtfmtStep.Style.DROPBOX -> config.dropboxStyle()
            KtfmtStep.Style.GOOGLE -> config.googleStyle()
            KtfmtStep.Style.KOTLINLANG -> config.kotlinlangStyle()
            else -> throw IllegalArgumentException("Unsupported ktfmt default style")
        }.configure(options::applyTo)
}
