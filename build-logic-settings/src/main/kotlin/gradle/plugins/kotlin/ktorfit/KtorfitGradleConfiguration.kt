package gradle.plugins.kotlin.ktorfit

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.accessors.catalog.libs
import gradle.accessors.ktorfit
import gradle.accessors.settings
import gradle.api.trySet
import org.gradle.api.Project

internal interface KtorfitGradleConfiguration {

    val generateQualifiedTypeName: Boolean?
    val errorCheckingMode: ErrorCheckingMode?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("ktorfit").id) {
            project.ktorfit::generateQualifiedTypeName trySet generateQualifiedTypeName
            project.ktorfit::errorCheckingMode trySet errorCheckingMode
        }
}
