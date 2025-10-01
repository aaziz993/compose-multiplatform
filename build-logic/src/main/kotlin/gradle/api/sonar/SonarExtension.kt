package gradle.api.sonar

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.sonarqube.gradle.SonarExtension

public val Project.sonar: SonarExtension get() = the()

public fun Project.sonar(configure: SonarExtension.() -> Unit): Unit = extensions.configure(configure)
