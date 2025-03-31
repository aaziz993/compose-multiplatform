package gradle.api

import gradle.accessors.localProperties
import gradle.accessors.publishing
import gradle.collection.get
import gradle.reflect.get
import java.util.Properties
import net.pearx.kasechange.CaseFormat
import net.pearx.kasechange.formatter.format
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
internal fun <T : Any> T.resolve(
    providers: ProviderFactory,
    extra: ExtraPropertiesExtension,
    localProperties: Properties,
): T = DeepRecursiveFunction<Any, Any?> { obj ->
    when (obj) {
        is String -> obj.resolveValue(providers, extra, localProperties, this@resolve)
        is Map<*, *> -> obj.mapValues { (_, value) -> value?.let { callRecursive(it) } }
        is List<*> -> obj.map { value -> value?.let { callRecursive(it) } }
        else -> obj
    }
}(this) as T

internal fun String.resolveValue(
    providers: ProviderFactory,
    extra: ExtraPropertiesExtension,
    localProperties: Properties,
    obj: Any
): Any? {
    if (startsWith("$"))
        removePrefix("$").resolveReference(providers, extra, localProperties, obj)
    else this
}

private fun String.resolveReference(
    providers: ProviderFactory,
    extra: ExtraPropertiesExtension,
    localProperties: Properties,
    obj: Any
) =
    if (startsWith("$")) {
        removePrefix("$")
            .split(".")
            .let { keys ->
                when (keys[0]) {
                    "env" -> System.getenv()[CaseFormat.UPPER_UNDERSCORE.format(keys.drop(1))]
                    "gradle" -> providers.gradleProperty(CaseFormat.LOWER_DOT.format(keys.drop(1))).orNull
                    "extra" -> extra[CaseFormat.LOWER_DOT.format(keys.drop(1))]
                    "local" -> localProperties[CaseFormat.LOWER_DOT.format(keys.drop(1))]
                    else -> obj.get(*keys.toTypedArray())
                }
            }
    }
    else this

internal fun Any.resolveComponentReference(reference: String): Any? {
    if (reference.isEmpty()) {
        return this
    }

    val key = reference.substringBefore(".")

    var restReference = reference.substringAfter(".", "")

    return when (this) {
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
    }?.resolveComponentReference(restReference)
}
