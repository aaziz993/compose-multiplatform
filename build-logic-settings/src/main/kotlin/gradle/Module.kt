@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle

import org.gradle.api.Project
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.model.ModuleProperties

private const val MODULE_EXTRA_PROPERTIES = "org.jetbrains.amper.gradle.ext.moduleExtraProperties"

internal var Project.amperModuleExtraProperties: ModuleProperties
    get() = extraProperties[MODULE_EXTRA_PROPERTIES] as ModuleProperties
    set(value) {
        extraProperties[MODULE_EXTRA_PROPERTIES] = value
    }

internal val AmperModule.shouldSeparateResourceCollectorsExpectActual
    get() = rootFragment.platforms.size > 1

