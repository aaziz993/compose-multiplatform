package plugin.project.amperlike

import java.nio.file.Path
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.jetbrains.amper.gradle.getBindingMap
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.amperlike.model.AmperLikeModel
import plugin.project.amperlike.model.AmperLikeModule

private const val KNOWN_MODEL_EXT = "org.jetbrains.amper.like.gradle.ext.model"

internal var Gradle.knownAmperLikeModel: AmperLikeModel?
    get() = extraProperties.getOrNull<AmperLikeModel>(KNOWN_MODEL_EXT)
    set(value) {
        extraProperties[KNOWN_MODEL_EXT] = value
    }

private const val NON_AMPER_MODULE_EXT = "org.jetbrains.amper.like.project.ext.module"

internal var Project.nonAmperModule: AmperLikeModule?
    get() = extraProperties.getOrNull<AmperLikeModule>(NON_AMPER_MODULE_EXT)
    set(value) {
        extraProperties[NON_AMPER_MODULE_EXT] = value
    }

private const val MODULE_TO_PROJECT_EXT = "org.jetbrains.amper.like.gradle.ext.moduleToProject"

internal val Gradle.nonAmperModuleFilePathToProjectPath: MutableMap<Path, String>
    get() = extraProperties.getBindingMap(MODULE_TO_PROJECT_EXT)

private inline fun <reified T : Any> ExtraPropertiesExtension.getOrNull(name: String): T? {
    return if (has(name)) get(name) as T else null
}
