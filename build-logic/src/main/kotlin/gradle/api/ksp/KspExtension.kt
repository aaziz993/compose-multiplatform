package gradle.api.ksp

import com.google.devtools.ksp.gradle.KspExtension
import gradle.api.kotlin.mpp.kotlin
import klib.data.type.primitives.string.uppercaseFirstChar
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

private const val KSP_COMMON_MAIN_METADATA = "kspCommonMainMetadata"

public val Project.ksp: KspExtension get() = the()

public fun Project.ksp(configure: KspExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.kspConfigurations: List<Configuration>
    get() = configurations.filter { configuration ->
        configuration.name == KSP_COMMON_MAIN_METADATA || kotlin.targets.any { target ->
            configuration.name == "ksp${target.name.uppercaseFirstChar()}"
        }
    }

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun KspExtension.dependencies(vararg dependencyNotations: Any): Unit =
    project.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        project.dependencies {
            project.kspConfigurations.forEach { configuration ->
                dependencyNotations.forEach { dependencyNotation ->
                    add(configuration.name, dependencyNotation)
                }
            }
        }
    }
