package gradle.plugins.project.ci

import kotlinx.serialization.Serializable

@Serializable
internal data class CIs(
    val jbSpace: JBSpaceAutomation = JBSpaceAutomation(),
)
