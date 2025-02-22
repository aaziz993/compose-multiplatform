package plugin.project.android.model

import com.android.build.gradle.internal.coverage.JacocoOptions
import gradle.trySet
import kotlinx.serialization.Serializable

/** DSL object for configuring JaCoCo settings. */
@Serializable
internal data class JacocoOptions(
    val jacocoVersion: String? = null,
) {

    fun applyTo(options: JacocoOptions) {
        options::jacocoVersion trySet jacocoVersion
    }
}
