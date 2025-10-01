package gradle.api.kotlin.targets.web.node

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

public val Project.node: NodeJsRootExtension get() = the()

public fun Project.node(configure: NodeJsRootExtension.() -> Unit): Unit = extensions.configure(configure)

