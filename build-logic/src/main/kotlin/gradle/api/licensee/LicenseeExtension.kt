package gradle.api.licensee

import app.cash.licensee.LicenseeExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.licensee: LicenseeExtension get() = the()

public fun Project.licensee(configure: LicenseeExtension.() -> Unit): Unit = extensions.configure(configure)
