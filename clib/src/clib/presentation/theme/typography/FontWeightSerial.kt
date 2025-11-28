package clib.presentation.theme.typography

import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import klib.data.type.serialization.serializers.primitive.PrimitiveIntSerializer
import kotlinx.serialization.Serializable

public object FontWeightSerializer : PrimitiveIntSerializer<FontWeight>(
    FontSynthesis::class.simpleName!!,
    FontWeight::weight,
    ::FontWeight,
)

public typealias FontWeightSerial = @Serializable(with = FontWeightSerializer::class) FontWeight

