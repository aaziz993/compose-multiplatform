@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.csv

import app.softwork.serialization.csv.CSVDecoder
import app.softwork.serialization.csv.CSVFormat
import klib.data.type.cast
import klib.data.type.serialization.coders.any.AnyDecoder
import kotlinx.serialization.DeserializationStrategy

internal class CSVAnyDecoder(decoder: CSVDecoder) : AnyDecoder<CSVDecoder>(decoder) {

    private val csv = CSVFormat.Custom(decoder.configuration)

    override fun decodeValue(): Any = decoder.decodeLists()

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, value: Any?): T =
        csv.decodeFromLists(deserializer, value.cast())
}
