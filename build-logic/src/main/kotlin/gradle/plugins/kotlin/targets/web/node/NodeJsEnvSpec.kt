package gradle.plugins.kotlin.targets.web.node

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec

public val Project.nodeJsEnv: NodeJsEnvSpec get() = the()

public fun Project.nodeEnv(configure: NodeJsEnvSpec.() -> Unit): Unit = extensions.configure(configure)
