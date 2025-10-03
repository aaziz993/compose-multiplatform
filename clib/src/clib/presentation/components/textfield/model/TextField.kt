package clib.presentation.components.textfield.model

import klib.data.type.serialization.isEnum
import klib.data.type.serialization.primitiveTypeOrNull
import kotlin.reflect.typeOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementNames

public sealed interface TextField {
    public data class Enum(val values: List<String>) : TextField
    public data object LocalTime : TextField
    public data object LocalDate : TextField
    public data object LocalDateTime : TextField
    public data object Text : TextField

    public companion object{
        @OptIn(ExperimentalSerializationApi::class)
        public operator fun invoke(descriptor: SerialDescriptor): TextField = if (descriptor.isEnum) {
            Enum(descriptor.elementNames.toList())
        }
        else when (descriptor.primitiveTypeOrNull) {
            typeOf<kotlinx.datetime.LocalTime>() -> LocalTime
            typeOf<kotlinx.datetime.LocalDate>() -> LocalDate
            typeOf<kotlinx.datetime.LocalDateTime>() -> LocalDateTime
            else -> Text
        }
    }
}
