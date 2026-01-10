package clib.presentation.theme.typography

import androidx.compose.ui.text.font.FontSynthesis
import klib.data.type.serialization.serializers.primitive.IntSerializer
import kotlinx.serialization.Serializable

public object FontSynthesisSerializer : IntSerializer<FontSynthesis>(
    FontSynthesis::class.simpleName!!,
    FontSynthesis::value,
    FontSynthesis::valueOf,
)

public typealias FontSynthesisSerial = @Serializable(with = FontSynthesisSerializer::class) FontSynthesis

