package gradle.collection

import gradle.act
import java.util.*
import net.pearx.kasechange.CaseFormat
import net.pearx.kasechange.formatter.format
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.ProviderFactory

public infix fun <E> MutableCollection<E>.tryAddAll(value: Iterable<E>?): Boolean? =
    value?.let(::addAll)

public infix fun <E> MutableCollection<E>.trySet(value: Iterable<E>?): Boolean? =
    tryAddAll(value?.act(::clear))

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



