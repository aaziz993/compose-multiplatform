package gradle.plugins.initialization

import gradle.api.initialization.problemreporter.ProblemReporterContext
import gradle.api.initialization.problemreporter.SLF4JProblemReporter

internal class SLF4JProblemReporterContext() : ProblemReporterContext {

    override val problemReporter: SLF4JProblemReporter = SLF4JProblemReporter(SettingsPlugin::class.java)
}
