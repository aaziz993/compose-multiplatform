package clib.data.type

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import klib.data.type.serialization.serializers.primitive.PrimitiveFloatSerializer
import kotlinx.serialization.Serializable

public object DpSerializer : PrimitiveFloatSerializer<Dp>(
    Dp::class.simpleName!!,
    Dp::value,
    Float::dp,
)

public typealias DpSerial = @Serializable(with = DpSerializer::class) Dp

