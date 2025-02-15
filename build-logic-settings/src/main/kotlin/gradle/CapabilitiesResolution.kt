package gradle

import org.gradle.api.artifacts.CapabilitiesResolution
import org.gradle.api.artifacts.CapabilityResolutionDetails

internal fun CapabilitiesResolution.withCapability(notation: Any, action: (CapabilityResolutionDetails) -> Unit) =
    withCapability(notation) { action(this) }

