package gradle.collection

import java.util.*
import net.pearx.kasechange.toDotCase
import net.pearx.kasechange.toScreamingSnakeCase
import net.pearx.kasechange.universalWordSplitter
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.ProviderFactory

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
    return resolveReference(providers, extra, localProperties, obj)
}

private fun String.resolveReference(
    providers: ProviderFactory,
    extra: ExtraPropertiesExtension,
    localProperties: Properties,
    obj: Any
) =
    if (startsWith("$")) {
        val key = substringAfter(".")
        removePrefix("$")
            .let(universalWordSplitter()::splitToWords)
            .let { keys ->
                when (keys[0]) {
                    "env" -> System.getenv()[key.toScreamingSnakeCase()]
                    "gradle" -> providers.gradleProperty(key.toDotCase()).orNull
                    "extra" -> extra[key.toDotCase()]
                    "local" -> localProperties[key.toDotCase()]
                    else -> obj.get(*keys.toTypedArray())
                }
            }
    }
    else this
