package plugin.project.kotlin.ktorfit.model

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode

internal interface KtorfitGradleConfiguration {

    val generateQualifiedTypeName: Boolean?
    val errorCheckingMode: ErrorCheckingMode?
}
