package gradle.plugins.android

import com.android.build.api.dsl.SigningConfig
import kotlinx.serialization.Serializable

@Serializable
internal data class SigningConfigDslImpl(
    override val storeFile: String? = null,
    override val storePassword: String? = null,
    override val keyAlias: String? = null,
    override val keyPassword: String? = null,
    override val storeType: String? = null,
    override val initWith: String? = null,
) : SigningConfigDsl<SigningConfig>
