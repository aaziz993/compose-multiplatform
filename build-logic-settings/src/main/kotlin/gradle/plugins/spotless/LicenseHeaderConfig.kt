package gradle.plugins.spotless

import com.diffplug.gradle.spotless.FormatExtension
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
){
    fun applyTo(license: FormatExtension.LicenseHeaderConfig){
        name?.let(license::named)
        contentPattern?.let(license::onlyIfContentMatches)
        yearSeparator?.let(license::yearSeparator)
        skipLinesMatching?.let(license::skipLinesMatching)
        updateYearWithLatest?.let(license::updateYearWithLatest)
    }
}
