package plugin.project.kotlin.ktorfit.model

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode

internal interface KtorfitGradleConfiguration {

    var generateQualifiedTypeName: Boolean?
    var errorCheckingMode: ErrorCheckingMode?
}
