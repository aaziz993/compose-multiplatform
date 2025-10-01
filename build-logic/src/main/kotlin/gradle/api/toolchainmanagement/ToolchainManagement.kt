@file:Suppress("UnstableApiUsage")

package gradle.api.toolchainmanagement

import org.gradle.api.Project
import org.gradle.api.toolchain.management.ToolchainManagement
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.toolchain: ToolchainManagement get() = the()

public fun Project.toolchain(configure: ToolchainManagement.() -> Unit): Unit = extensions.configure(configure)
