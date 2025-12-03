@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.csv

import app.softwork.serialization.csv.CSVEncoder
import app.softwork.serialization.csv.CSVFormat
import klib.data.type.cast
import klib.data.type.collections.list.drop
import klib.data.type.serialization.coders.any.AnyEncoder
import kotlinx.serialization.SerializationStrategy

internal class CSVAnyEncoder(encoder: CSVEncoder) : AnyEncoder<CSVEncoder>(encoder) {

    private val csv = CSVFormat.Custom(encoder.configuration)

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T): Any =
        csv.encodeToLists(serializer, value).let { lists ->
            if (encoder.configuration.includeHeader) lists.drop() else lists
        }

    override fun encodeValue(value: Any?): Unit = encoder.encodeLists(value.cast())
}
