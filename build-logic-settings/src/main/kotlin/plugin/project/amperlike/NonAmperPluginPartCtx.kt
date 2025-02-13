package plugin.project.amperlike

import java.nio.file.Path
import org.gradle.api.Project
import org.jetbrains.amper.frontend.Model

internal open class AmperLikePluginPartCtx(
    override val project: Project,
    override val model: Model,
    override val module: AmperLikeModuleWrapper,
    override val moduleToProject: Map<Path, String>,
) : AmperLikeBindingPluginPart
