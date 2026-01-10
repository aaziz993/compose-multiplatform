package clib.presentation.theme.typography

import androidx.compose.ui.text.font.FontStyle
import klib.data.type.serialization.serializers.primitive.IntSerializer
import kotlinx.serialization.Serializable

public object FontStyleSerializer : IntSerializer<FontStyle>(
    FontStyle::class.simpleName!!,
    FontStyle::value,
    ::FontStyle,
)

public typealias FontStyleSerial = @Serializable(with = FontStyleSerializer::class) FontStyle

