package gradle.plugins.spotless

import com.diffplug.gradle.spotless.FormatExtension
import kotlinx.serialization.Serializable

@Serializable
internal sealed class LicenseHeaderConfig {

    abstract val name: String?
    abstract val contentPattern: String?
    abstract val header: String?
    abstract val headerFile: String?
    abstract val delimiter: String?
    abstract val yearSeparator: String?
    abstract val skipLinesMatching: String?
    abstract val updateYearWithLatest: Boolean?

    fun applyTo(license: FormatExtension.LicenseHeaderConfig) {
        name?.let(license::named)
        contentPattern?.let(license::onlyIfContentMatches)
        yearSeparator?.let(license::yearSeparator)
        skipLinesMatching?.let(license::skipLinesMatching)
        updateYearWithLatest?.let(license::updateYearWithLatest)
    }
}
