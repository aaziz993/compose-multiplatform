package klib.data.type.serialization.xml

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import nl.adaptivity.xmlutil.*
import nl.adaptivity.xmlutil.core.impl.multiplatform.StringWriter
import nl.adaptivity.xmlutil.core.impl.multiplatform.use
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlConfig
import kotlin.invoke

public fun <T> XML.encodeToMap(serializer: SerializationStrategy<T>, value: T): Map<XmlEvent.StartElementEvent, Any> =
    decodeMapFromString(encodeToString(serializer, value))

public inline fun <reified T> XML.encodeToMap(value: T): Map<XmlEvent.StartElementEvent, Any> =
    encodeToMap(serializersModule.serializer(), value)

public fun <T> XML.decodeFromMap(
    deserializer: DeserializationStrategy<T>,
    value: Map<XmlEvent.StartElementEvent, Any>
): T = decodeFromString(deserializer, encodeMapToString(value))

public inline fun <reified T> XML.decodeFromMap(value: Map<XmlEvent.StartElementEvent, Any>): T =
    decodeFromMap(serializersModule.serializer(), value)

public fun XML.decodeMapFromString(value: String): Map<XmlEvent.StartElementEvent, Any> = when {
    config.defaultToGenericParser -> xmlStreaming.newGenericReader(value)
    else -> xmlStreaming.newReader(value)
}.decodeMapFromReader()

public fun XmlReader.decodeMapFromReader(): Map<XmlEvent.StartElementEvent, Any> = buildMap {
    if (!isStarted)
        while (hasNext() && next() != EventType.START_DOCUMENT) Unit

    DECODE_MAP_FROM_READER_DEEP_RECURSIVE_FUNCTION(
        DecodeMapFromReaderArgs(
            ((if (isStarted) sequenceOf(toEvent()) else emptySequence()) +
                    this@decodeMapFromReader.asSequence().map { eventType ->
                        eventType.createEvent(this@decodeMapFromReader)
                    }).iterator(),
            this,
            readOnlyElement = eventType != EventType.START_DOCUMENT
        )
    )
}

private data class DecodeMapFromReaderArgs(
    val sequence: Iterator<XmlEvent>,
    val value: MutableMap<XmlEvent.StartElementEvent, Any>,
    val keyEvent: XmlEvent.StartElementEvent? = null,
    val valueEvent: XmlEvent? = null,
    val readOnlyElement: Boolean = false
)

private val DECODE_MAP_FROM_READER_DEEP_RECURSIVE_FUNCTION =
    DeepRecursiveFunction<DecodeMapFromReaderArgs, Unit> { (iterator, value, keyEvent, valueEvent, readOnlyElement) ->
        var keyEvent: XmlEvent.StartElementEvent? = keyEvent
        var valueEvent: XmlEvent? = valueEvent

        iterator.forEach { event ->
            if (!event.isIgnorable)
                when (event) {
                    is XmlEvent.StartElementEvent ->
                        if (keyEvent == null) keyEvent = event
                        else {
                            callRecursive(
                                DecodeMapFromReaderArgs(
                                    iterator,
                                    mutableMapOf<XmlEvent.StartElementEvent, Any>().also { map ->
                                        value.put(keyEvent!!, map)
                                        keyEvent = null
                                    },
                                    event,
                                    readOnlyElement = false
                                )
                            )

                            if (readOnlyElement) return@DeepRecursiveFunction
                        }

                    is XmlEvent.EndElementEvent ->
                        if (keyEvent == null) return@DeepRecursiveFunction
                        else {
                            value.put(keyEvent, valueEvent ?: event)

                            if (readOnlyElement) return@DeepRecursiveFunction

                            keyEvent = null
                            valueEvent = null
                        }

                    else -> valueEvent = event
                }
        }
    }

public fun XML.encodeMapToString(value: Map<XmlEvent.StartElementEvent, Any>): String {
    val stringWriter = StringWriter()

    val xw = when {
        config.defaultToGenericParser -> xmlStreaming.newGenericWriter(
            stringWriter,
            config.repairNamespaces,
            config.xmlDeclMode
        )

        else -> xmlStreaming.newWriter(stringWriter, config.repairNamespaces, config.xmlDeclMode)
    }

    xw.use { xmlWriter ->
        xmlWriter.indentString = config.indentString

        when (config.xmlDeclMode) {
            XmlDeclMode.Minimal -> {
                xmlWriter.startDocument(config.xmlVersion.versionString)
            }

            XmlDeclMode.Charset -> {
                // TODO support non-utf8 encoding
                xmlWriter.startDocument(config.xmlVersion.versionString, encoding = "UTF-8")
            }

            XmlDeclMode.None,
            XmlDeclMode.Auto -> Unit // no implementation needed
        }

        value.encodeMapToWriter(xmlWriter)

        xmlWriter.endDocument()
    }

    return stringWriter.toString()
}

public fun Map<XmlEvent.StartElementEvent, Any>.encodeMapToWriter(xmlWriter: XmlWriter): Unit =
    ENCODE_MAP_TO_WRITER_DEEP_RECURSIVE_FUNCTION(EncodeMapToWriterArgs(this, xmlWriter))

private data class EncodeMapToWriterArgs(
    val value: Map<XmlEvent.StartElementEvent, Any>,
    val xmlWriter: XmlWriter
)

@Suppress("UNCHECKED_CAST")
private val ENCODE_MAP_TO_WRITER_DEEP_RECURSIVE_FUNCTION =
    DeepRecursiveFunction<EncodeMapToWriterArgs, Unit> { (value, xmlWriter) ->
        value.forEach { (key, value) ->
            key.writeTo(xmlWriter)

            if (value is XmlEvent) {
                value.writeTo(xmlWriter)
                if (value is XmlEvent.EndElementEvent) return@forEach
            } else callRecursive(
                EncodeMapToWriterArgs(value as Map<XmlEvent.StartElementEvent, Any>, xmlWriter)
            )

            key.asEndElementEvent.writeTo(xmlWriter)
        }
    }

private val XmlEvent.StartElementEvent.asEndElementEvent
    get() = XmlEvent.EndElementEvent(extLocationInfo, namespaceUri, localName, prefix, namespaceContext)

public fun Map<XmlEvent.StartElementEvent, Any>.encodeXml(format: XML = XML.defaultInstance): String =
    format.encodeMapToString(this)

public fun String.decodeXml(format: XML = XML.defaultInstance): Map<XmlEvent.StartElementEvent, Any> =
    format.decodeMapFromString(this)
