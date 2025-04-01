package gradle.plugins.kotlin.ktorfit

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.accessors.ktorfit
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KtorfitGradleConfiguration(
    val generateQualifiedTypeName: Boolean? = null,
    val errorCheckingMode: ErrorCheckingMode? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("de.jensklingenberg.ktorfit") {
            project.ktorfit::generateQualifiedTypeName trySet generateQualifiedTypeName
            project.ktorfit::errorCheckingMode trySet errorCheckingMode
        }
}
