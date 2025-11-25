package clib.presentation.shapes

import androidx.compose.ui.unit.Dp
import klib.data.type.serialization.serializers.primitive.PrimitiveFloatSerializer
import kotlinx.serialization.Serializable

public object DpSerializer :
    PrimitiveFloatSerializer<Dp>(
        Dp::class.simpleName!!,
        Dp::value,
        ::Dp,
    )

public typealias DpSerial = @Serializable(with = DpSerializer::class) Dp

