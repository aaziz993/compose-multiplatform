package gradle.plugins.kotlin.ktorfit

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.accessors.ktorfit
import gradle.api.trySet
import org.gradle.api.Project

internal interface KtorfitGradleConfiguration {

    val generateQualifiedTypeName: Boolean?
    val errorCheckingMode: ErrorCheckingMode?

    context(Project)
    fun applyTo() {
        ktorfit::generateQualifiedTypeName trySet generateQualifiedTypeName
        ktorfit::errorCheckingMode trySet errorCheckingMode
    }
}
