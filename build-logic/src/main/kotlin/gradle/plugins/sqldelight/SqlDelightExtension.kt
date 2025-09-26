package gradle.plugins.sqldelight

import app.cash.sqldelight.gradle.SqlDelightExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.sqldelight: SqlDelightExtension get() = the()

public fun Project.sqldelight(configure: SqlDelightExtension.() -> Unit): Unit = extensions.configure(configure)
