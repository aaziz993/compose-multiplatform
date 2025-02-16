package plugin.project.gradle.spotless.model

import com.diffplug.spotless.generic.LicenseHeaderStep.YearMode
import kotlinx.serialization.Serializable

@Serializable
internal data class LicenseHeaderConfig(
    val name: String? = null,
    val contentPattern: String? = null,
    val header: String? = null,
    val headerFile: String? = null,
    val delimiter: String = "^",
    val yearSeparator: String? = null,
    val skipLinesMatching: String? = null,
    val updateYearWithLatest: Boolean? = null
)
