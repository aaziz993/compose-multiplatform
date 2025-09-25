package klib.data.type.serialization.csv

import app.softwork.serialization.csv.CSVDecoder
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.decodeStructure

private val listDescriptor = listSerialDescriptor<String>()

public fun CSVDecoder.decodeLists(): List<List<Any?>> = listOf(buildList {
    decodeStructure(listDescriptor) {
        while (true) {
            val index = decodeElementIndex(listDescriptor)

            if (index == CompositeDecoder.DECODE_DONE)
                break

            try {
                add(decodeStringElement(listDescriptor, index))
            } catch (_: SerializationException) {
                break
            }
        }
    }
})

