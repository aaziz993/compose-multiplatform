package gradle.plugins.kotlin.ktorfit

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.accessors.id
import gradle.accessors.ktorfit
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import org.gradle.api.Project

internal interface KtorfitGradleConfiguration {

    val generateQualifiedTypeName: Boolean?
    val errorCheckingMode: ErrorCheckingMode?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("ktorfit").id) {
            ktorfit::generateQualifiedTypeName trySet generateQualifiedTypeName
            ktorfit::errorCheckingMode trySet errorCheckingMode
        }
}
