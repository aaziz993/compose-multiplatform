package klib.data.cache

import com.russhwolf.settings.Settings
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public class SettingsCache<K : Any, V : Any>(
    private val keyKClass: KClass<K>,
    private val valueKClass: KClass<V>
) : Cache<K, V> {

    private val settings: Settings by lazy { Settings() }

    private val inKey: (key: K) -> String = if (keyKClass == String::class) {
        { it as String }
    }
    else {
        { Json.Default.encodeToString(keyKClass.serializer(), it) }
    }

    private val outKey: (key: String) -> K = if (keyKClass == String::class) {
        { it as K }
    }
    else {
        { Json.Default.decodeFromString(keyKClass.serializer(), it) }
    }

    private val toMap: () -> Map<K, V> = when (valueKClass) {
        Boolean::class -> {
            { settings.keys.associate { key -> outKey(key) to settings.getBooleanOrNull(key)!! as V } }
        }

        Int::class -> {
            { settings.keys.associate { key -> outKey(key) to settings.getIntOrNull(key)!! as V } }
        }

        Long::class -> {
            { settings.keys.associate { key -> outKey(key) to settings.getLongOrNull(key)!! as V } }
        }

        Float::class -> {
            { settings.keys.associate { key -> outKey(key) to settings.getFloatOrNull(key)!! as V } }
        }

        Double::class -> {
            { settings.keys.associate { key -> outKey(key) to settings.getDoubleOrNull(key)!! as V } }
        }

        String::class, Any::class -> {
            { settings.keys.associate { key -> outKey(key) to settings.getStringOrNull(key)!! as V } }
        }

        else -> {
            {
                settings.keys.associate { key ->
                    outKey(key) to settings.getStringOrNull(key)!!.let {
                        Json.Default.decodeFromString(valueKClass.serializer(), it)
                    }
                }
            }
        }
    }

    override fun get(key: K): V? = inKey(key).let { inKey ->
        when (valueKClass) {
            Boolean::class -> settings.getBooleanOrNull(inKey) as V
            Int::class -> settings.getIntOrNull(inKey) as V
            Long::class -> settings.getLongOrNull(inKey) as V
            Float::class -> settings.getFloatOrNull(inKey) as V
            Double::class -> settings.getDoubleOrNull(inKey) as V
            String::class -> settings.getStringOrNull(inKey) as V
            else -> settings.getStringOrNull(inKey)?.let { value ->
                Json.Default.decodeFromString(valueKClass.serializer(), value)
            }
        }
    }

    override fun set(key: K, value: V): Unit = inKey(key).let { inKey ->
        when (value) {
            is Boolean -> settings.putBoolean(inKey, value)
            is Int -> settings.putInt(inKey, value)
            is Long -> settings.putLong(inKey, value)
            is Float -> settings.putFloat(inKey, value)
            is Double -> settings.putDouble(inKey, value)
            is String -> settings.putString(inKey, value)
            else -> settings.putString(inKey, Json.Default.encodeToString(valueKClass.serializer(), value))
        }
    }

    override fun remove(key: K): Unit = settings.remove(inKey(key))

    override fun clear(): Unit = settings.clear()

    override fun asMap(): Map<K, V> = toMap()
}
