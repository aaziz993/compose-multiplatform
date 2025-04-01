package gradle.api

import gradle.accessors.localProperties
import gradle.accessors.publishing
import java.util.*
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.DependencySubstitutions
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@Suppress("UNCHECKED_CAST")
public fun <T : Any> T.resolve(
    context: Any
): T {
    val sources = listOf(context, this)
    return DeepRecursiveFunction<Any, Any?> { obj ->
        when (obj) {
            is String -> sources.resolveValue(obj)
            is Map<*, *> -> obj.mapValues { (_, value) -> value?.let { callRecursive(it) } }
            is List<*> -> obj.map { value -> value?.let { callRecursive(it) } }
            else -> obj
        }
    }(this) as T
}

public fun List<Any>.resolveValue(
    reference: String,
): Any? {
    if (reference.startsWith("$"))
        reference.removePrefix("$").let { reference ->
            firstNotNullOfOrNull { obj -> obj.get(reference) }
        }
    else this
}

public operator fun Any.get(reference: String): Any? {
    val key = reference.substringBefore(".")

    var restReference = reference.substringAfter(".", "")

    val resolved = when (this) {
        is Boolean,
        is Byte,
        is Short,
        is Int,
        is Long,
        is Float,
        is Double,
        is Char,
        is String -> return this

        is System -> when (key) {
            "getenv" -> System.getenv()
            else -> this[key]
        }

        is Settings -> when (key) {
            "providers" -> providers
            "extra" -> extra
            "extraProperties" -> extraProperties
            "localProperties" -> localProperties
            else -> this[key]
        }

        is Project -> when (key) {
            "providers" -> providers
            "extra" -> extra
            "extraProperties" -> extraProperties
            "localProperties" -> localProperties
            "configurations" -> configurations
            "publishing" -> publishing
            else -> this[key]
        }

        is ExtraPropertiesExtension -> this[key]

        is ProviderFactory -> when (key) {
            "gradleProperty" -> {
                gradleProperty(restReference.substringBefore("."))
                restReference.substringAfter(".", "")
            }

            else -> this[key]
        }

        is Properties -> getProperty(key)

        is ConfigurationContainer -> getByNameOrAll(key)

        is Configuration -> when (key) {
            "allArtifacts" -> allArtifacts
            else -> this[key]
        }

        is PublishingExtension -> when (key) {
            "publications" -> publications
            else -> this[key]
        }

        is PublicationContainer -> getByNameOrAll(key)

        is DependencySubstitutions -> when (key) {
            "project" -> {
                project(restReference.substringBefore("."))
                restReference = restReference.substringAfter(".", "")
            }

            "module" -> {
                module(restReference.substringBefore("."))
                restReference = restReference.substringAfter(".", "")
            }

            else -> this[key]
        }

        is List<*> -> {
            filterIsInstance<Configuration>()
        }

        else -> this[key]
    }

    return resolved

//        ?.resolveComponentReference(restReference)
}
