package plugin.project.kotlin.ktorfit.model

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.trySet
import kotlinx.serialization.Serializable

@Serializable
internal data class KtorfitSettings(
    override val generateQualifiedTypeName: Boolean? = null,
    override val errorCheckingMode: ErrorCheckingMode? = null,
    val enabled: Boolean = true
) : KtorfitGradleConfiguration {

    fun applyTo(configuration: de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration) {
        configuration::generateQualifiedTypeName trySet generateQualifiedTypeName
        configuration::errorCheckingMode trySet errorCheckingMode
    }
}
