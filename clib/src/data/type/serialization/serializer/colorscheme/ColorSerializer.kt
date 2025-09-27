package data.type.serialization.serializer.colorscheme

import androidx.compose.ui.graphics.Color
import klib.data.type.serialization.serializers.primitive.PrimitiveLongSerializer
import kotlinx.serialization.Serializable

public object ColorSerializer :
    PrimitiveLongSerializer<Color>(
        Color::class.simpleName!!,
        { it.value.toLong() },
        { Color(it.toULong()) },
    )

public typealias ColorSerial = @Serializable(with = ColorSerializer::class) Color

