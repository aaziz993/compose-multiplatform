package plugin.settings.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.model.BuildCache


@Serializable
internal data class RemoteBuildCache(
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
) : BuildCache
