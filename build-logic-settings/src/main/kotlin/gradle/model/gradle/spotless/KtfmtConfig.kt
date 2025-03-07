package gradle.model.gradle.spotless

import com.diffplug.spotless.kotlin.KtfmtStep
import kotlinx.serialization.Serializable

@Serializable
internal data class KtfmtConfig(
     val version: String? = null,
     val style: KtfmtStep.Style,
     val options: KtfmtFormattingOptions
)
