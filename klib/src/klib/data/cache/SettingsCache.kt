package klib.data.cache

import com.russhwolf.settings.Settings
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public class SettingsCache<K : Any, V : Any>(
    private val keyEncoder: (K) -> String = { key -> key as String },
    private val keyDecoder: (String) -> K = { key -> key as K },
    private val valueKClass: KClass<V>,
    private val valueEncoder: (V) -> String = { value ->
        Json.Default.encodeToString(valueKClass.serializer(), value)
    },
    private val valueDecoder: (String) -> V = { value ->
        Json.Default.decodeFromString(valueKClass.serializer(), value)
    },
) : Cache<K, V> {

    private val settings: Settings by lazy { Settings() }

    override fun get(key: K): V? = get(keyEncoder(key))

    private fun get(key: String): V? =
        when (valueKClass) {
            Boolean::class -> settings.getBooleanOrNull(key) as V
            Int::class -> settings.getIntOrNull(key) as V
            Long::class -> settings.getLongOrNull(key) as V
            Float::class -> settings.getFloatOrNull(key) as V
            Double::class -> settings.getDoubleOrNull(key) as V
            String::class -> settings.getStringOrNull(key) as V
            else -> settings.getStringOrNull(key)?.let(valueDecoder)
        }

    override fun set(key: K, value: V) {
        val encodedKey = keyEncoder(key)

        when (value) {
            is Boolean -> settings.putBoolean(encodedKey, value)
            is Int -> settings.putInt(encodedKey, value)
            is Long -> settings.putLong(encodedKey, value)
            is Float -> settings.putFloat(encodedKey, value)
            is Double -> settings.putDouble(encodedKey, value)
            is String -> settings.putString(encodedKey, value)
            else -> settings.putString(encodedKey, valueEncoder(value))
        }
    }

    override fun remove(key: K): Unit = settings.remove(keyEncoder(key))

    override fun clear(): Unit = settings.clear()

    override fun asMap(): Map<K, V> =
        settings.keys.associate { key -> keyDecoder(key) to get(key)!! }
}
