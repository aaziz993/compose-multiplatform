package gradle.plugins.kover

import gradle.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverReportsConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Configuration of Kover reports.
 *
 * An individual set of reports is created for each Kover report variant.
 * All these sets can be configured independently of each other.
 *
 * The main difference between the reports sets and the report variants is that the reports are individual for each project, the settings of reports in different projects do not affect each other in any way.
 * At the same time, changing a report variant affects all reports that are based on it, for example, if several projects import a variant through a dependency `kover(project(":subproject"))`.
 *
 * Example of usage:
 * ```
 *  kover {
 *      reports {
 *          filters {
 *              // common filters for all reports of all variants
 *          }
 *          verify {
 *              // common verification rules for all variants
 *          }
 *
 *          /*
 *          Total reports set - special reports for all code of current project and it's kover dependencies.
 *          These are the reports for total variant of current project and it's kover dependencies.
 *          */
 *          total {
 *              // config
 *          }
 *
 *          /*
 *          Configure custom reports set with name "custom".
 *          These are the reports for variant "custom" of current project and it's kover dependencies.
 *          */
 *          variant("custom") {
 *          }
 *      }
 *  }
 * ```
 */
@Serializable
internal data class KoverReportsConfig(
    /**
     * Instance to configuring of common filters for all report variants.
     *
     * See details in [filters].
     */
    val filters: KoverReportFiltersConfig? = null,
    /**
     * Instance to configuring of common verification rules for all report variants.
     *
     * See details in [verify].
     */
    val verify: KoverVerificationRulesConfig? = null,
    /**
     * Instance to configuring of reports for all code of current project and `kover` dependencies.
     *
     * See details in [total].
     */
    public val total: KoverReportSetConfig? = null,
) {

    context(Project)
    fun applyTo(reports: KoverReportsConfig) {
        filters?.let { filters ->
            reports.filters(filters::applyTo)
        }

        verify?.let { verify ->
            reports.verify(verify::applyTo)
        }

        total?.let { total ->
            reports.total {
                total.filters?.let { filters ->
                    filters(filters::applyTo)
                }

                total.html?.let { html ->
                    html {
                        html.applyTo(this)
                    }
                }

                total.xml?.let { xml ->
                    xml {
                        xml.applyTo(this)
                    }
                }

                total.binary?.let { binary ->
                    binary {
                        binary.applyTo(this)
                    }
                }
                total.verify?.let { verify ->
                    verify(verify::applyTo)
                }

                total.log?.let { log ->
                    log (log::applyTo)
                }

                additionalBinaryReports tryAssign total.additionalBinaryReports?.map(::file)
            }
        }
    }
}
