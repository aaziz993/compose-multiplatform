package clib.presentation.theme.typography

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.text.font.SystemFontFamily
import klib.data.type.serialization.serializers.primitive.StringSerializer
import kotlinx.serialization.Serializable

public object FontFamilySerializer : StringSerializer<FontFamily>(
    FontFamily::class.simpleName!!,
    { fontFamily ->
        when (fontFamily) {
            is SystemFontFamily -> "default"
            is GenericFontFamily -> fontFamily.name
            else -> throw IllegalArgumentException("Unknown font family '$fontFamily'")
        }
    },
    { name ->
        when (name) {
            "default" -> FontFamily.Default
            "sans-serif" -> FontFamily.SansSerif
            "serif" -> FontFamily.Serif
            "monospace" -> FontFamily.Monospace
            "cursive" -> FontFamily.Cursive
            else -> throw IllegalArgumentException("Unknown font family '$name'")
        }
    },
)

public typealias FontFamilySerial = @Serializable(with = FontFamilySerializer::class) FontFamily

