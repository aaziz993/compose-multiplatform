package gradle.api.kotlin.web.yarn

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

public val Project.yarn: YarnRootExtension get() = the()

public fun Project.yarn(configure: YarnRootExtension.() -> Unit): Unit = extensions.configure(configure)
