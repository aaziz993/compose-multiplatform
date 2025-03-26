package gradle.plugins.kover.reports

import gradle.plugins.kover.reports.total.KoverReportSetConfig
import kotlinx.serialization.Serializable

@Serializable
internal data class Variant(
    val variant: String,
    val config: KoverReportSetConfig
)
