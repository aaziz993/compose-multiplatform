package gradle.plugins.android

import com.android.build.api.dsl.PrivacySandbox
import gradle.reflect.trySet
import kotlinx.serialization.Serializable

/**
 * Privacy Sandbox library consumption options
 *
 * TODO LINK
 */
@Serializable
internal data class PrivacySandbox(
    val enable: Boolean? = null,
) {

    fun applyTo(receiver: PrivacySandbox) {
        receiver::enable trySet enable
    }
}
