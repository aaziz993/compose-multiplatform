package gradle.caching

import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteBuildCache(
    val type: String? = null,
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
) : BuildCache
