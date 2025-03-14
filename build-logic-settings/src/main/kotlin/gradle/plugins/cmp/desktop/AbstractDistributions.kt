package gradle.plugins.cmp.desktop

import gradle.accessors.libs
import gradle.accessors.moduleName
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.tryAssign
import gradle.api.trySet
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
