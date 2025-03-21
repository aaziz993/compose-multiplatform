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

internal abstract class AbstractDistributions<T : AbstractDistributions> {

    abstract val outputBaseDir: String?
    abstract val packageName: String?
    abstract val packageVersion: String?
    abstract val copyright: String?
    abstract val description: String?
    abstract val vendor: String?
    abstract val appResourcesRootDir: String?
    abstract val licenseFile: String?

    context(Project)
    open fun applyTo(recipient: T) {
        recipient.outputBaseDir tryAssign outputBaseDir?.let(layout.projectDirectory::dir)
        recipient.packageName = packageName ?: moduleName
        recipient::packageVersion trySet (packageVersion
            ?: settings.libs.versions.version("compose.desktop.packageVersion"))
        recipient::copyright trySet copyright
        recipient::description trySet description
        recipient::vendor trySet vendor
        recipient.appResourcesRootDir tryAssign appResourcesRootDir?.let(layout.projectDirectory::dir)
        recipient.licenseFile tryAssign licenseFile?.let(::file)
    }
}
