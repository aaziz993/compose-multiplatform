package plugin.project.kotlin.ktorfit.model

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class KtorfitSettings(
    override var generateQualifiedTypeName: Boolean? = null,
    override var errorCheckingMode: ErrorCheckingMode? = null,
    val enabled: Boolean = true
) : KtorfitGradleConfiguration {

    fun applyTo(configuration: de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration) {
        configuration::generateQualifiedTypeName trySet generateQualifiedTypeName
        configuration::errorCheckingMode trySet errorCheckingMode
    }
}
