package gradle.collection

import java.util.Properties
import net.pearx.kasechange.toDotCase
import net.pearx.kasechange.toScreamingSnakeCase
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.ProviderFactory

internal fun Any.resolve(
    providers: ProviderFactory,
    extra: ExtraPropertiesExtension,
    localProperties: Properties,
): Any? = DeepRecursiveFunction<Any, Any?> { obj ->
    when (obj) {
        is String -> obj.resolveValue(providers, extra, localProperties)
        is Map<*, *> -> obj.mapValues { (_, value) -> value?.let { callRecursive(it) } }
        is List<*> -> obj.map { value -> value?.let { callRecursive(it) } }
        else -> obj
    }
}(this)

internal fun String.resolveValue(
    providers: ProviderFactory,
    extra: ExtraPropertiesExtension,
    localProperties: Properties,
): Any? {
    return resolveReference(providers, extra, localProperties)
}

private fun String.resolveReference(
    providers: ProviderFactory,
    extra: ExtraPropertiesExtension,
    localProperties: Properties,
) =
    if (startsWith("$")) {
        val key = substringAfter(".")
        removePrefix("$")
            .substringBefore(".")
            .split("|")
            .map(String::lowercase)
            .firstNotNullOfOrNull { reference ->
                when (reference) {
                    "env" -> System.getenv()[key.toScreamingSnakeCase()]
                    "gradle" -> providers.gradleProperty(key.toDotCase()).orNull
                    "extra" -> extra[key.toDotCase()]
                    "local" -> localProperties[key.toDotCase()]
                    else -> this
                }
            }
    }
    else this
