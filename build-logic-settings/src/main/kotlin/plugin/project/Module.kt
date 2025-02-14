@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import org.gradle.api.Project
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.model.Properties

private const val MODULE_EXTRA_PROPERTIES = "org.jetbrains.amper.gradle.ext.moduleExtraProperties"

internal var Project.amperModuleExtraProperties: Properties
    get() = extraProperties[MODULE_EXTRA_PROPERTIES] as Properties
    set(value) {
        extraProperties[MODULE_EXTRA_PROPERTIES] = value
    }

internal val AmperModule.shouldSeparateResourceCollectorsExpectActual
    get() = rootFragment.platforms.size > 1

