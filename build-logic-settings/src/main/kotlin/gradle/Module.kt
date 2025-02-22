@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.model.Properties

private const val MODULE_EXTRA_PROPERTIES = "moduleExtraProperties"

internal var Project.moduleProperties: Properties
    get() = extraProperties[MODULE_EXTRA_PROPERTIES] as Properties
    set(value) {
        extraProperties[MODULE_EXTRA_PROPERTIES] = value
    }

