package gradle.model.cmp.desktop.model

import gradle.libs
import gradle.moduleName
import gradle.settings
import gradle.tryAssign
import gradle.trySet
import gradle.version
import gradle.versions
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.AbstractDistributions

internal abstract class AbstractDistributions {

    abstract val outputBaseDir: String?
    abstract val packageName: String?
    abstract val packageVersion: String?
    abstract val copyright: String?
    abstract val description: String?
    abstract val vendor: String?
    abstract val appResourcesRootDir: String?
    abstract val licenseFile: String?

    context(Project)
    fun applyTo(distributions: AbstractDistributions) {
        distributions.outputBaseDir tryAssign outputBaseDir?.let(layout.projectDirectory::dir)
        distributions.packageName = packageName ?: moduleName
        distributions::packageVersion trySet (packageVersion
            ?: settings.libs.versions.version("compose.desktop.packageVersion"))
        distributions::copyright trySet copyright
        distributions::description trySet description
        distributions::vendor trySet vendor
        distributions.appResourcesRootDir tryAssign appResourcesRootDir?.let(layout.projectDirectory::dir)
        distributions.licenseFile tryAssign licenseFile?.let(::file)
    }
}
