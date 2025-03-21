package gradle.plugins.cmp.desktop

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
    open fun applyTo(recipient: T) {
        recipient.iconFile tryAssign iconFile?.let(::file)
        recipient::packageVersion trySet packageVersion
        recipient::installationPath trySet installationPath
        fileAssociations?.forEach { (mimeType, extension, description, iconFile) ->
            recipient.fileAssociation(mimeType, extension, description, iconFile?.let(::file))
        }
    }
}

