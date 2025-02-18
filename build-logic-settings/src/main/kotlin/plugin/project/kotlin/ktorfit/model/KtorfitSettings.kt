package plugin.project.kotlin.ktorfit.model

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import kotlinx.serialization.Serializable

@Serializable
internal data class KtorfitSettings(
    override var generateQualifiedTypeName: Boolean? = null,
    override var errorCheckingMode: ErrorCheckingMode? = null,
    val enabled: Boolean = true
) : KtorfitGradleConfiguration
