package gradle.model.gradle.spotless

import com.diffplug.spotless.kotlin.KtfmtStep
import kotlinx.serialization.Serializable

@Serializable
internal data class KtfmtFormattingOptions(
    val maxWidth: Int? = null,
    val blockIndent: Int? = null,
    val continuationIndent: Int? = null,
    val removeUnusedImport: Boolean? = null,
) {

    fun applyTo(options: KtfmtStep.KtfmtFormattingOptions) {
        maxWidth?.let(options::setMaxWidth)
        blockIndent?.let(options::setBlockIndent)
        continuationIndent?.let(options::setContinuationIndent)
        removeUnusedImport?.let(options::setRemoveUnusedImport)
    }
}
