package clib.presentation.theme.typography

import androidx.compose.ui.text.font.FontSynthesis
import klib.data.type.serialization.serializers.primitive.PrimitiveIntSerializer
import kotlinx.serialization.Serializable

public object FontSynthesisSerializer : PrimitiveIntSerializer<FontSynthesis>(
    FontSynthesis::class.simpleName!!,
    FontSynthesis::value,
    FontSynthesis::valueOf,
)

public typealias FontSynthesisSerial = @Serializable(with = FontSynthesisSerializer::class) FontSynthesis

