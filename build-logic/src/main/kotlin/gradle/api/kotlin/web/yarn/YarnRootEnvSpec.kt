package gradle.api.kotlin.web.yarn

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootEnvSpec

public val Project.yarnEnv: YarnRootEnvSpec get() = the()

public fun Project.yarnEnv(configure: YarnRootEnvSpec.() -> Unit): Unit = extensions.configure(configure)
