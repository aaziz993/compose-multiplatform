package clib.data.type

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import klib.data.type.serialization.serializers.primitive.FloatSerializer
import kotlinx.serialization.Serializable

public object SpSerializer : FloatSerializer<TextUnit>(
    TextUnit::class.simpleName!!,
    TextUnit::value,
    Float::sp,
)

public typealias SpSerial = @Serializable(with = SpSerializer::class) TextUnit

