package klib.data.type.serialization.csv

import app.softwork.serialization.csv.CSVEncoder
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.encodeStructure

private val listDescriptor = listSerialDescriptor<Any?>()

@Suppress("UNCHECKED_CAST")
public fun CSVEncoder.encodeLists(value: List<List<Any?>>) {
    value.forEach { elements ->
        encodeStructure(listDescriptor) {
            elements.forEach { element ->
                when (element) {
                    null -> encodeNull()
                    is Float -> encodeFloat(element)
                    is Double -> encodeDouble(element)
                    is Enum<*> -> encodeString(element.ordinal.toString())
                    else -> encodeString(element.toString())
                }
            }
        }
    }
}
