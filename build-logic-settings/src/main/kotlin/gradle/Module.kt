@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.model.ModuleProperties

private const val MODULE_EXTRA_PROPERTIES = "moduleExtraProperties"

internal var Project.moduleProperties: ModuleProperties
    get() = extraProperties[MODULE_EXTRA_PROPERTIES] as ModuleProperties
    set(value) {
        extraProperties[MODULE_EXTRA_PROPERTIES] = value
    }

