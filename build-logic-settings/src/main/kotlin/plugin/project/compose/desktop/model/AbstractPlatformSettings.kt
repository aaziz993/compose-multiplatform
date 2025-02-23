package plugin.project.compose.model

import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.AbstractPlatformSettings

internal abstract class AbstractPlatformSettings {

    abstract val iconFile: String?
    abstract val packageVersion: String?
    abstract val installationPath: String?

    abstract val fileAssociations: List<FileAssociation>?

    context(Project)
    fun applyTo(settings: AbstractPlatformSettings) {
        settings.iconFile tryAssign iconFile?.let(::file)
        settings::packageVersion trySet packageVersion
        settings::installationPath trySet installationPath
        fileAssociations?.forEach { (mimeType, extension, description, iconFile) ->
            settings.fileAssociation(mimeType, extension, description, iconFile?.let(::file))
        }
    }
}

