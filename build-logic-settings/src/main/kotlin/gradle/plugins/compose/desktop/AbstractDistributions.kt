package gradle.plugins.compose.desktop

import gradle.accessors.moduleName
import gradle.accessors.settings
import gradle.api.file.tryAssign
import gradle.api.libs
import gradle.api.provider.tryAssign
import klib.data.type.reflection.trySet
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
    open fun applyTo(receiver: T) {
        receiver.outputBaseDir tryAssign outputBaseDir?.let(project.layout.projectDirectory::dir)
        receiver.packageName = packageName ?: moduleName
        receiver::packageVersion trySet (packageVersion
            ?: project.settings.libs.versionOrNull("compose.desktop.packageVersion"))
        receiver::copyright trySet copyright
        receiver::description trySet description
        receiver::vendor trySet vendor
        receiver.appResourcesRootDir tryAssign appResourcesRootDir?.let(project.layout.projectDirectory::dir)
        receiver.licenseFile tryAssign licenseFile?.let(project::file)
    }
}
