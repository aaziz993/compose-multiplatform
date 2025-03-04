package plugin.project.android.model

import com.android.builder.signing.DefaultSigningConfig
import kotlinx.serialization.Serializable

/** DSL object to configure signing configs. */
@Serializable
internal data class DefaultSigningConfig(
    override val storeFile: String? = null,
    override val storePassword: String? = null,
    override val keyAlias: String? = null,
    override val keyPassword: String? = null,
    override val storeType: String? = null,
    override val name: String,
    override val isSigningReady: Boolean,
) : SigningConfig
