package clib.data.type

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import klib.data.type.serialization.serializers.primitive.PrimitiveFloatSerializer
import kotlinx.serialization.Serializable

public object SpSerializer : PrimitiveFloatSerializer<TextUnit>(
    TextUnit::class.simpleName!!,
    TextUnit::value,
    Float::sp,
)

public typealias SpSerial = @Serializable(with = SpSerializer::class) TextUnit

