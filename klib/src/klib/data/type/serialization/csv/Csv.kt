@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.csv

import app.softwork.serialization.csv.*
import app.softwork.serialization.csv.CSVNode
import klib.data.type.collections.iterator.chunked
import klib.data.type.collections.iterator.intersperse
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.serializer

internal fun <T> CSVFormat.encodeToNodes(serializer: SerializationStrategy<T>, value: T): List<CSVNode> =
    encodeToString(serializer, value)
        .parse(configuration.separator, configuration.lineSeparator)
        .asSequence().toList()

internal inline fun <reified T> CSVFormat.encodeToNodes(value: T): List<CSVNode> =
    encodeToNodes(serializersModule.serializer(), value)

@Suppress("UNCHECKED_CAST")
internal fun CSVFormat.encodeListsToNodes(value: List<List<Any?>>): List<CSVNode> =
    value.map { elements -> elements.map(::encodeAnyToCSVElement) }.iterator()
        .intersperse { listOf(CSVNode.NewLine) }.flatten()

private fun CSVFormat.encodeAnyToCSVElement(value: Any?) = CSVNode.Element(
        buildString {
            val valueToAppend =
                    when (value) {
                        is Float, Double ->
                            when (configuration.numberFormat) {
                                CSVFormat.NumberFormat.Dot -> value
                                CSVFormat.NumberFormat.Comma -> value.toString().replace(".", ",")
                            }

                        is Enum<*> -> value.ordinal

                        else -> value
                    }?.toString().orEmpty()

            val quote =
                    configuration.alwaysEmitQuotes || configuration.separator in valueToAppend || configuration.lineSeparator in valueToAppend
            if (quote) append('"')

            append(valueToAppend)

            if (quote) append('"')
        },
)

@Suppress("UNCHECKED_CAST")
internal fun CSVFormat.decodeListsFromNodes(value: List<CSVNode>): List<List<Any?>> =
    value.iterator().chunked { node -> node == CSVNode.NewLine }.map { nodes ->
        (nodes as List<CSVNode.Element>).map(::decodeAnyFromCSVElement)
    }

private fun CSVFormat.decodeAnyFromCSVElement(node: CSVNode.Element): Any? {
    var value = node.value

    val quote =
        configuration.alwaysEmitQuotes || configuration.separator in value || configuration.lineSeparator in value
    if (quote)
        value = value.removePrefix("\"").removeSuffix("\"")


    return """(-?\d+)(?:[.,](\d+))?""".toRegex().matchEntire(value)?.let { matchResult ->
        if (matchResult.groupValues[2].isEmpty()) matchResult.value.toLong()
        else when (CSVFormat.NumberFormat.Comma) {
            CSVFormat.NumberFormat.Dot -> matchResult.value
            CSVFormat.NumberFormat.Comma -> matchResult.value.replace(",", ".")
        }.toDouble()
    } ?: value.ifEmpty { null }
}

public fun <T> CSVFormat.encodeToLists(serializer: SerializationStrategy<T>, value: T): List<List<Any?>> =
    decodeListsFromNodes(encodeToNodes(serializer, value))

public inline fun <reified T> CSVFormat.encodeToLists(value: T): List<List<Any?>> =
    encodeToLists(serializersModule.serializer(), value)

public fun <T> CSVFormat.decodeFromLists(deserializer: DeserializationStrategy<T>, value: List<List<Any?>>): T {
    deserializer.descriptor.checkForLists()

    val parsed = encodeListsToNodes(value).iterator().stateful()

    return if (configuration.includeHeader) {
        val headers = deserializer.descriptor.flatNames.asSequence().toList()

        val isSequentially = headers.isSequentially(deserializer.descriptor)
        deserializer.deserialize(
                decoder = CSVDecoderImpl(
                        header = headers,
                        nodes = parsed,
                        configuration = configuration,
                        decodesSequentially = isSequentially,
                        level = if (deserializer.descriptor.kind is StructureKind.LIST) -1 else 0,
                ),
        )
    }
    else {
        val decodesSequentially = deserializer.descriptor.kind !is StructureKind.LIST
        deserializer.deserialize(
                decoder = CSVDecoderImpl(
                        header = emptyList(),
                        nodes = parsed,
                        configuration = configuration,
                        decodesSequentially = decodesSequentially,
                        level = if (deserializer.descriptor.kind is StructureKind.LIST) -1 else 0,
                ),
        )
    }
}

public inline fun <reified T> CSVFormat.decodeFromLists(value: List<List<Any?>>): T =
    decodeFromLists(serializersModule.serializer(), value)

@Suppress("UNCHECKED_CAST")
public fun CSVFormat.encodeListsToString(value: List<List<Any?>>): String = buildString {
    CSVEncoderImpl(this, configuration).encodeLists(value)
}

public fun CSVFormat.decodeListsFromString(value: String): List<List<Any?>> =
    decodeListsFromNodes(
            value.parse(configuration.separator, configuration.lineSeparator)
                    .asSequence()
                    .toList(),
    )

public fun String.encodeCsv(format: CSVFormat = CSVFormat.Default): List<List<Any?>> =
    format.decodeListsFromString(this)

public fun List<List<Any?>>.decodeCsv(format: CSVFormat = CSVFormat.Default): String =
    format.encodeListsToString(this)
