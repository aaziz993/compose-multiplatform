package plugin.gradle

import org.jetbrains.amper.frontend.Model
import org.jetbrains.amper.frontend.schema.ComposeSettings
import org.jetbrains.amper.frontend.schema.commonSettings
import org.jetbrains.kotlin.gradle.internal.config.MavenComparableVersion

/**
 * Try to find single compose version within model.
 */
internal fun chooseComposeVersion(model: Model) = model.modules
    .map { it.origin.commonSettings.compose }
    .filter(ComposeSettings::enabled)
    .mapNotNull(ComposeSettings::version)
    .maxWithOrNull(compareBy(::MavenComparableVersion))
