package plugin.settings.model

import kotlinx.serialization.Serializable


@Serializable
internal data class RemoteBuildCache(
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
) : BuildCache
