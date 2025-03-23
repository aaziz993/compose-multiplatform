package gradle.plugins.cmp.desktop.platform

import gradle.api.tryAssign
import gradle.api.trySet
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.AbstractPlatformSettings

internal abstract class AbstractPlatformSettings<T : AbstractPlatformSettings> {

    abstract val iconFile: String?
    abstract val packageVersion: String?
    abstract val installationPath: String?

    abstract val fileAssociations: Set<FileAssociation>?

    context(Project)
    open fun applyTo(receiver: T) {
        receiver.iconFile tryAssign iconFile?.let(::file)
        receiver::packageVersion trySet packageVersion
        receiver::installationPath trySet installationPath
        fileAssociations?.forEach { (mimeType, extension, description, iconFile) ->
            receiver.fileAssociation(mimeType, extension, description, iconFile?.let(::file))
        }
    }
}

