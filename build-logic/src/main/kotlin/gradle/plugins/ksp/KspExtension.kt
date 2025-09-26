package gradle.plugins.ksp

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.ksp: KspExtension get() = the()

public fun Project.ksp(configure: KspExtension.() -> Unit): Unit = extensions.configure(configure)
