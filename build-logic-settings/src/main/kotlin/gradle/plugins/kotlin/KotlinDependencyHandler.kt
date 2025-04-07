package gradle.plugins.kotlin

import gradle.api.artifacts.Dependency
import kotlinx.serialization.Serializable
import org.gradle.api.internal.tasks.JvmConstants
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@Serializable
internal data class KotlinDependencyConfiguration(
    val configurationName: String,
    val dependencyNotation: Dependency<out org.gradle.api.artifacts.Dependency>
) {

    val configurationFunction: KotlinDependencyHandler.(Any) -> org.gradle.api.artifacts.Dependency?
        get() = when (configurationName) {
            JvmConstants.API_CONFIGURATION_NAME -> KotlinDependencyHandler::api
            JvmConstants.IMPLEMENTATION_CONFIGURATION_NAME -> KotlinDependencyHandler::implementation
            JvmConstants.COMPILE_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::compileOnly
            JvmConstants.RUNTIME_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::runtimeOnly
            else -> error("Unsupported dependency configuration: $configurationName")
        }
}

/**
 * Represents a DSL for managing the dependencies of Kotlin entities that implement a [HasKotlinDependencies] interface.
 */
@Serializable
internal data class KotlinDependencyHandler(
    val dependencies: Set<KotlinDependencyConfiguration>? = null,
) {

    fun applyTo(receiver: KotlinDependencyHandler) {
        dependencies?.forEach { dependency ->
            when (dependency.dependencyNotation) {
                is Project -> receiver.project(dependency.dependencyNotation.path)
                is Kotlin -> receiver.kotlin(dependency.dependencyNotation.simpleModuleName)
                is Npm -> receiver.kotlin(dependency.dependencyNotation.simpleModuleName)
            }
            dependency.configurationFunction
        }
    }
}
