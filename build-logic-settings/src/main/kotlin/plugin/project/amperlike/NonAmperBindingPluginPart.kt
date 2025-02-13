package plugin.project.amperlike

import java.nio.file.Path
import org.gradle.api.Project
import org.jetbrains.amper.frontend.Model
import plugin.project.amperlike.model.AmperLikeModule

internal interface AmperLikeBindingPluginPart {
    val project: Project
    val model: Model
    val module: AmperLikeModuleWrapper
    val moduleToProject: Map<Path, String>

    val AmperLikeModule.linkedProject
        get() = project.project(
            moduleToProject[moduleDir]
                ?: error("No linked Gradle project found for module $userReadableName")
        )

    val needToApply: Boolean get() = false

    /**
     * Logic, that needs to be applied before project evaluation.
     */
    fun applyBeforeEvaluate() {}

    /**
     * Logic, that needs to be applied after project evaluation.
     */
    fun applyAfterEvaluate() {}
}
