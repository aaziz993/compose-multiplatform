package klib.data.type.serialization.properties

import klib.data.type.collections.getOrPut
import klib.data.type.collections.isZeroConsecutive
import klib.data.type.collections.put
import klib.data.type.collections.unflattenKeys
import klib.data.type.primitives.string.asBufferedSource
import klib.data.type.serialization.coders.tree.serialize
import klib.data.type.serialization.serializers.any.NullableAnySerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.internal.FormatLanguage
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource

public open class Properties(
    public val configuration: PropertiesConfiguration = PropertiesConfiguration(),
    override val serializersModule: SerializersModule = EmptySerializersModule(),
) : StringFormat {

    //        @ThreadLocal
    public companion object Default : Properties()

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        val buffer = Buffer()
        encodeToBufferedSink(serializer, value, buffer)
        return buffer.readUtf8().trimEnd()
    }

    public fun <T> encodeToBufferedSink(serializer: SerializationStrategy<T>, value: T, sink: BufferedSink): Unit =
        PropertiesEncoder(PropertiesWriter(sink, configuration.escUnicode), this)
            .encodeSerializableValue(
                serializer,
                value,
            )

    public inline fun <reified T> encodeToBufferedSink(value: T, sink: BufferedSink): Unit =
        encodeToBufferedSink(serializersModule.serializer(), value, sink)

    public fun <T> encodeToAny(serializer: SerializationStrategy<T>, value: T): Any? =
        serializer.serialize(value, serializersModule, configuration.asTreeEncoderConfiguration)

    public inline fun <reified T> encodeToAny(value: T): Any? =
        encodeToAny(serializersModule.serializer(), value)

    public fun encodeAnyToString(value: Any): String = encodeToString(NullableAnySerializer, value)

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        decodeFromAny(deserializer, decodeAnyFromString(string))

    public fun <T> decodeFromBufferedSource(
        deserializer: DeserializationStrategy<T>,
        source: BufferedSource
    ): T = decodeFromAny(deserializer, decodeAnyFromString(source))

    public inline fun <reified T> decodeFromBufferedSource(source: BufferedSource): T =
        decodeFromBufferedSource(serializersModule.serializer(), source)

    public fun <T> decodeFromAny(deserializer: DeserializationStrategy<T>, value: Any?): T =
        deserializer.deserialize(PropertiesDecoder(value, serializersModule, configuration))

    public inline fun <reified T> decodeFromAny(value: Any?): T =
        decodeFromAny(serializersModule.serializer(), value)

    public fun decodeAnyFromString(
        @FormatLanguage("properties", "", "") string: String
    ): Any = decodeAnyFromString(string.asBufferedSource)

    public fun decodeAnyFromString(
        @FormatLanguage("properties", "", "") source: BufferedSource
    ): Any = PropertiesReader(source)
        .read()
        .map { (path, value) -> path.split(".") to value }
        .unflattenKeys<String, String>(
            destinationGetter = { sourceKeys ->
                if (isEmpty()) sourceKeys.toNewMutableCollection()
                else last().first.getOrPut(last().second, { sourceKeys.toNewMutableCollection() })
            }
        )

    private fun List<String>.toNewMutableCollection() =
        if (all { key -> key.matches("\\d+".toRegex()) } &&
            map(String::toInt).sorted().isZeroConsecutive())
            ArrayList<Any?>(size)
        else LinkedHashMap<Any?, Any?>(size)

}

public fun String.asProperties(format: Properties = Properties.Default): Any =
    format.decodeAnyFromString(this)
