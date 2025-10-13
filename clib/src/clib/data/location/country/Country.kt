package clib.data.location.country

import clib.generated.resources.Res
import clib.generated.resources.allDrawableResources
import clib.generated.resources.image_load_error
import klib.data.iso.Alpha2Letter
import org.jetbrains.compose.resources.DrawableResource

public val Alpha2Letter.flag: DrawableResource
    get() = Res.allDrawableResources["flag_${toString().lowercase()}"] ?: Res.drawable.image_load_error

public fun Alpha2Letter.getEmojiFlag(): String =
    toString().uppercase().map { char ->
        val codePoint = 0x1F1E6 + (char.code - 'A'.code)
        val highSurrogate = ((codePoint shr 10) + 0xD800 - (0x10000 shr 10)).toChar()
        val lowSurrogate = ((codePoint and 0x3FF) + 0xDC00).toChar()
        "$highSurrogate$lowSurrogate"
    }.joinToString("")
