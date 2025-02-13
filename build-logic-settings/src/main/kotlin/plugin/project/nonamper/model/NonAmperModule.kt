@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.nonamper.model

import org.gradle.api.Project
import org.jetbrains.amper.gradle.getOrNull
import org.jetbrains.kotlin.gradle.plugin.extraProperties

private const val NON_AMPER_MODULE_EXT = "org.jetbrains.non.amper.project.ext.module"

internal data class NonAmperModule(
    val type: NonAmperModuleType
)

internal var Project.nonAmperModule: NonAmperModule?
    get() = extraProperties.getOrNull<NonAmperModule>(NON_AMPER_MODULE_EXT)
    set(value) {
        extraProperties[NON_AMPER_MODULE_EXT] = value
    }
