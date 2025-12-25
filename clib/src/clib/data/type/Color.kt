package clib.data.type

import androidx.annotation.ColorInt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toArgb
import com.github.ajalt.colormath.model.LABColorSpaces.LAB50
import com.github.ajalt.colormath.model.Oklab
import com.github.ajalt.colormath.model.RGB
import com.github.ajalt.colormath.model.RGBColorSpaces.ACES
import com.github.ajalt.colormath.model.RGBColorSpaces.ACEScg
import com.github.ajalt.colormath.model.RGBColorSpaces.AdobeRGB
import com.github.ajalt.colormath.model.RGBColorSpaces.BT2020
import com.github.ajalt.colormath.model.RGBColorSpaces.BT709
import com.github.ajalt.colormath.model.RGBColorSpaces.DCI_P3
import com.github.ajalt.colormath.model.RGBColorSpaces.DisplayP3
import com.github.ajalt.colormath.model.RGBColorSpaces.LinearSRGB
import com.github.ajalt.colormath.model.RGBColorSpaces.ROMM_RGB
import com.github.ajalt.colormath.model.RGBInt
import com.github.ajalt.colormath.model.SRGB
import com.github.ajalt.colormath.model.XYZColorSpaces.XYZ50
import klib.data.type.serialization.serializers.primitive.PrimitiveULongSerializer
import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.Color as ComposeColor
import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.HSL

public object ColorSerializer : PrimitiveULongSerializer<ComposeColor>(
    ComposeColor::class.simpleName!!,
    { color -> color.toArgb().toULong() },
    { value -> ComposeColor(value.toInt()) },
)

public typealias ColorSerial = @Serializable(with = ColorSerializer::class) ComposeColor

/**
 * Material colors 500
 */
public val ComposeColor.Companion.Rose: ComposeColor
    get() = ComposeColor(0xFFE91E63)
public val ComposeColor.Companion.LPurple: ComposeColor
    get() = ComposeColor(0xFF9C27B0)
public val ComposeColor.Companion.DPurple: ComposeColor
    get() = ComposeColor(0xFF673AB7)
public val ComposeColor.Companion.DBlue: ComposeColor
    get() = ComposeColor(0xFF3F51B5)
public val ComposeColor.Companion.LBlue: ComposeColor
    get() = ComposeColor(0xFF2196F3)
public val ComposeColor.Companion.LLBlue: ComposeColor
    get() = ComposeColor(0xFF03A9F4)
public val ComposeColor.Companion.LCyan: ComposeColor
    get() = ComposeColor(0xFF00BCD4)
public val ComposeColor.Companion.DCyan: ComposeColor
    get() = ComposeColor(0xFF009688)
public val ComposeColor.Companion.DGreen: ComposeColor
    get() = ComposeColor(0xFF4CAF50)
public val ComposeColor.Companion.LGreen: ComposeColor
    get() = ComposeColor(0xFF8BC34A)
public val ComposeColor.Companion.LLGreen: ComposeColor
    get() = ComposeColor(0xFFCDDC39)
public val ComposeColor.Companion.Gold: ComposeColor
    get() = ComposeColor(0xFFFFC107)
public val ComposeColor.Companion.Orange: ComposeColor
    get() = ComposeColor(0xFFFF9800)

public fun ComposeColor.Companion.getColorMap(): Map<String, ComposeColor> = mapOf(
    "Red" to Red,
    "Rose" to Rose,
    "LPurple" to LPurple,
    "DPurple" to DPurple,
    "DBlue" to DBlue,
    "LBlue" to LBlue,
    "LLBlue" to LLBlue,
    "LCyan" to LCyan,
    "DCyan" to DCyan,
    "DGreen" to DGreen,
    "LGreen" to LGreen,
    "LLGreen" to LLGreen,
    "Yellow" to Yellow,
    "Gold" to Gold,
    "Orange" to Orange,
)

/**
 * Material colors 900
 * This is for internal usage only.
 */
internal object Color900 {

    val Red: ComposeColor = ComposeColor(0xFFB71C1C)
    val Rose: ComposeColor = ComposeColor(0xFF880E4F)
    val LPurple: ComposeColor = ComposeColor(0xFF4A148C)
    val DPurple: ComposeColor = ComposeColor(0xFF311B92)
    val DBlue: ComposeColor = ComposeColor(0xFF1A237E)
    val LBlue: ComposeColor = ComposeColor(0xFF0D47A1)
    val LLBlue: ComposeColor = ComposeColor(0xFF01579B)
    val LCyan: ComposeColor = ComposeColor(0xFF006064)
    val DCyan: ComposeColor = ComposeColor(0xFF004D40)
    val DGreen: ComposeColor = ComposeColor(0xFF1B5E20)
    val LGreen: ComposeColor = ComposeColor(0xFF33691E)
    val LLGreen: ComposeColor = ComposeColor(0xFF827717)
    val Yellow: ComposeColor = ComposeColor(0xFFF57F17)
    val Gold: ComposeColor = ComposeColor(0xFFFF6F00)
    val Orange: ComposeColor = ComposeColor(0xFFE65100)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 800
 * This is for internal usage only.
 */
internal object Color800 {

    val Red: ComposeColor = ComposeColor(0xFFC62828)
    val Rose: ComposeColor = ComposeColor(0xFFAD1457)
    val LPurple: ComposeColor = ComposeColor(0xFF6A1B9A)
    val DPurple: ComposeColor = ComposeColor(0xFF4527A0)
    val DBlue: ComposeColor = ComposeColor(0xFF283593)
    val LBlue: ComposeColor = ComposeColor(0xFF1565C0)
    val LLBlue: ComposeColor = ComposeColor(0xFF0277BD)
    val LCyan: ComposeColor = ComposeColor(0xFF00838F)
    val DCyan: ComposeColor = ComposeColor(0xFF00695C)
    val DGreen: ComposeColor = ComposeColor(0xFF2E7D32)
    val LGreen: ComposeColor = ComposeColor(0xFF558B2F)
    val LLGreen: ComposeColor = ComposeColor(0xFF9E9D24)
    val Yellow: ComposeColor = ComposeColor(0xFFF9A825)
    val Gold: ComposeColor = ComposeColor(0xFFFF8F00)
    val Orange: ComposeColor = ComposeColor(0xFFEF6C00)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 700
 * This is for internal usage only.
 */
internal object Color700 {

    val Red: ComposeColor = ComposeColor(0xFFD32F2F)
    val Rose: ComposeColor = ComposeColor(0xFFC2185B)
    val LPurple: ComposeColor = ComposeColor(0xFF7B1FA2)
    val DPurple: ComposeColor = ComposeColor(0xFF512DA8)
    val DBlue: ComposeColor = ComposeColor(0xFF303F9F)
    val LBlue: ComposeColor = ComposeColor(0xFF1976D2)
    val LLBlue: ComposeColor = ComposeColor(0xFF0288D1)
    val LCyan: ComposeColor = ComposeColor(0xFF0097A7)
    val DCyan: ComposeColor = ComposeColor(0xFF00796B)
    val DGreen: ComposeColor = ComposeColor(0xFF388E3C)
    val LGreen: ComposeColor = ComposeColor(0xFF689F38)
    val LLGreen: ComposeColor = ComposeColor(0xFFAFB42B)
    val Yellow: ComposeColor = ComposeColor(0xFFFBC02D)
    val Gold: ComposeColor = ComposeColor(0xFFFFA000)
    val Orange: ComposeColor = ComposeColor(0xFFF57C00)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 600
 * This is for internal usage only.
 */
internal object Color600 {

    val Red: ComposeColor = ComposeColor(0xFFE53935)
    val Rose: ComposeColor = ComposeColor(0xFFD81B60)
    val LPurple: ComposeColor = ComposeColor(0xFF8E24AA)
    val DPurple: ComposeColor = ComposeColor(0xFF5E35B1)
    val DBlue: ComposeColor = ComposeColor(0xFF3949AB)
    val LBlue: ComposeColor = ComposeColor(0xFF1E88E5)
    val LLBlue: ComposeColor = ComposeColor(0xFF039BE5)
    val LCyan: ComposeColor = ComposeColor(0xFF00ACC1)
    val DCyan: ComposeColor = ComposeColor(0xFF00897B)
    val DGreen: ComposeColor = ComposeColor(0xFF43A047)
    val LGreen: ComposeColor = ComposeColor(0xFF7CB342)
    val LLGreen: ComposeColor = ComposeColor(0xFFC0CA33)
    val Yellow: ComposeColor = ComposeColor(0xFFFDD835)
    val Gold: ComposeColor = ComposeColor(0xFFFFB300)
    val Orange: ComposeColor = ComposeColor(0xFFFB8C00)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 400
 * This is for internal usage only.
 */
internal object Color400 {

    val Red: ComposeColor = ComposeColor(0xFFEF5350)
    val Rose: ComposeColor = ComposeColor(0xFFEC407A)
    val LPurple: ComposeColor = ComposeColor(0xFFAB47BC)
    val DPurple: ComposeColor = ComposeColor(0xFF7E57C2)
    val DBlue: ComposeColor = ComposeColor(0xFF5C6BC0)
    val LBlue: ComposeColor = ComposeColor(0xFF42A5F5)
    val LLBlue: ComposeColor = ComposeColor(0xFF29B6F6)
    val LCyan: ComposeColor = ComposeColor(0xFF26C6DA)
    val DCyan: ComposeColor = ComposeColor(0xFF26A69A)
    val DGreen: ComposeColor = ComposeColor(0xFF66BB6A)
    val LGreen: ComposeColor = ComposeColor(0xFF9CCC65)
    val LLGreen: ComposeColor = ComposeColor(0xFFD4E157)
    val Yellow: ComposeColor = ComposeColor(0xFFFFEE58)
    val Gold: ComposeColor = ComposeColor(0xFFFFCA28)
    val Orange: ComposeColor = ComposeColor(0xFFFFA726)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 300
 * This is for internal usage only.
 */
internal object Color300 {

    val Red: ComposeColor = ComposeColor(0xFFE57373)
    val Rose: ComposeColor = ComposeColor(0xFFF06292)
    val LPurple: ComposeColor = ComposeColor(0xFFBA68C8)
    val DPurple: ComposeColor = ComposeColor(0xFF9575CD)
    val DBlue: ComposeColor = ComposeColor(0xFF7986CB)
    val LBlue: ComposeColor = ComposeColor(0xFF64B5F6)
    val LLBlue: ComposeColor = ComposeColor(0xFF4FC3F7)
    val LCyan: ComposeColor = ComposeColor(0xFF4DD0E1)
    val DCyan: ComposeColor = ComposeColor(0xFF4DB6AC)
    val DGreen: ComposeColor = ComposeColor(0xFF81C784)
    val LGreen: ComposeColor = ComposeColor(0xFFAED581)
    val LLGreen: ComposeColor = ComposeColor(0xFFDCE775)
    val Yellow: ComposeColor = ComposeColor(0xFFFFF176)
    val Gold: ComposeColor = ComposeColor(0xFFFFD54F)
    val Orange: ComposeColor = ComposeColor(0xFFFFB74D)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 200
 * This is for internal usage only.
 */
internal object Color200 {

    val Red: ComposeColor = ComposeColor(0xFFEF9A9A)
    val Rose: ComposeColor = ComposeColor(0xFFF48FB1)
    val LPurple: ComposeColor = ComposeColor(0xFFCE93D8)
    val DPurple: ComposeColor = ComposeColor(0xFFB39DDB)
    val DBlue: ComposeColor = ComposeColor(0xFF9FA8DA)
    val LBlue: ComposeColor = ComposeColor(0xFF90CAF9)
    val LLBlue: ComposeColor = ComposeColor(0xFF81D4FA)
    val LCyan: ComposeColor = ComposeColor(0xFF80DEEA)
    val DCyan: ComposeColor = ComposeColor(0xFF80CBC4)
    val DGreen: ComposeColor = ComposeColor(0xFFA5D6A7)
    val LGreen: ComposeColor = ComposeColor(0xFFC5E1A5)
    val LLGreen: ComposeColor = ComposeColor(0xFFE6EE9C)
    val Yellow: ComposeColor = ComposeColor(0xFFFFF59D)
    val Gold: ComposeColor = ComposeColor(0xFFFFE082)
    val Orange: ComposeColor = ComposeColor(0xFFFFCC80)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 100
 * This is for internal usage only.
 */
internal object Color100 {

    val Red: ComposeColor = ComposeColor(0xFFFFCDD2)
    val Rose: ComposeColor = ComposeColor(0xFFF8BBD0)
    val LPurple: ComposeColor = ComposeColor(0xFFE1BEE7)
    val DPurple: ComposeColor = ComposeColor(0xFFD1C4E9)
    val DBlue: ComposeColor = ComposeColor(0xFFC5CAE9)
    val LBlue: ComposeColor = ComposeColor(0xFFBBDEFB)
    val LLBlue: ComposeColor = ComposeColor(0xFFB3E5FC)
    val LCyan: ComposeColor = ComposeColor(0xFFB2EBF2)
    val DCyan: ComposeColor = ComposeColor(0xFFB2DFDB)
    val DGreen: ComposeColor = ComposeColor(0xFFC8E6C9)
    val LGreen: ComposeColor = ComposeColor(0xFFDCEDC8)
    val LLGreen: ComposeColor = ComposeColor(0xFFF0F4C3)
    val Yellow: ComposeColor = ComposeColor(0xFFFFF9C4)
    val Gold: ComposeColor = ComposeColor(0xFFFFECB3)
    val Orange: ComposeColor = ComposeColor(0xFFFFE0B2)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

/**
 * Material colors 50
 * This is for internal usage only.
 */
internal object Color50 {

    val Red: ComposeColor = ComposeColor(0xFFFFEBEE)
    val Rose: ComposeColor = ComposeColor(0xFFFCE4EC)
    val LPurple: ComposeColor = ComposeColor(0xFFF3E5F5)
    val DPurple: ComposeColor = ComposeColor(0xFFEDE7F6)
    val DBlue: ComposeColor = ComposeColor(0xFFE8EAF6)
    val LBlue: ComposeColor = ComposeColor(0xFFE3F2FD)
    val LLBlue: ComposeColor = ComposeColor(0xFFE1F5FE)
    val LCyan: ComposeColor = ComposeColor(0xFFE0F7FA)
    val DCyan: ComposeColor = ComposeColor(0xFFE0F2F1)
    val DGreen: ComposeColor = ComposeColor(0xFFE8F5E9)
    val LGreen: ComposeColor = ComposeColor(0xFFF1F8E9)
    val LLGreen: ComposeColor = ComposeColor(0xFFF9FBE7)
    val Yellow: ComposeColor = ComposeColor(0xFFFFFDE7)
    val Gold: ComposeColor = ComposeColor(0xFFFFF8E1)
    val Orange: ComposeColor = ComposeColor(0xFFFFF3E0)

    fun getColorMap(): Map<String, ComposeColor> = mapOf(
        "Red" to Red,
        "Rose" to Rose,
        "LPurple" to LPurple,
        "DPurple" to DPurple,
        "DBlue" to DBlue,
        "LBlue" to LBlue,
        "LLBlue" to LLBlue,
        "LCyan" to LCyan,
        "DCyan" to DCyan,
        "DGreen" to DGreen,
        "LGreen" to LGreen,
        "LLGreen" to LLGreen,
        "Yellow" to Yellow,
        "Gold" to Gold,
        "Orange" to Orange,
    )
}

@Composable
public fun ComposeColor.orErrorColor(isError: Boolean): ComposeColor =
    if (isError) MaterialTheme.colorScheme.error else this

/**
 * Convert this color to a ColorMath [Color] instance.
 */
public fun ComposeColor.toColor(): Color =
    when (colorSpace) {
        ColorSpaces.Srgb -> SRGB(red, green, blue, alpha)
        ColorSpaces.Aces -> ACES(red, green, blue, alpha)
        ColorSpaces.Acescg -> ACEScg(red, green, blue, alpha)
        ColorSpaces.AdobeRgb -> AdobeRGB(red, green, blue, alpha)
        ColorSpaces.Bt2020 -> BT2020(red, green, blue, alpha)
        ColorSpaces.Bt709 -> BT709(red, green, blue, alpha)
        ColorSpaces.CieLab -> LAB50(red, green, blue, alpha)
        ColorSpaces.CieXyz -> XYZ50(red, green, blue, alpha)
        ColorSpaces.DciP3 -> DCI_P3(red, green, blue, alpha)
        ColorSpaces.DisplayP3 -> DisplayP3(red, green, blue, alpha)
        ColorSpaces.LinearSrgb -> LinearSRGB(red, green, blue, alpha)
        ColorSpaces.ProPhotoRgb -> ROMM_RGB(red, green, blue, alpha)
        else -> convert(ColorSpaces.Srgb).let { SRGB(it.red, it.green, it.blue, it.alpha) }
    }

/**
 * Convert this color to a ColorMath [SRGB] instance.
 */
public fun ComposeColor.toSRGB(): RGB =
    convert(ColorSpaces.Srgb).let { SRGB(it.red, it.green, it.blue, it.alpha) }

/**
 * Convert this color to a ColorMath [HSL] instance.
 */
public fun ComposeColor.toHSL(): HSL = toColor().toHSL().let { hsl -> if (hsl.h.isNaN()) hsl.copy(h = 0f) else hsl }

/**
 * Convert this color to a Compose [ComposeColor] instance.
 */
public fun Color.toColor(): ComposeColor {
    if (this is RGBInt) return ComposeColor(argb.toInt())
    val s = when {
        space == SRGB -> ColorSpaces.Srgb
        space === ACES -> ColorSpaces.Aces
        space === ACEScg -> ColorSpaces.Acescg
        space === AdobeRGB -> ColorSpaces.AdobeRgb
        space === BT2020 -> ColorSpaces.Bt2020
        space === BT709 -> ColorSpaces.Bt709
        space === LAB50 -> ColorSpaces.CieLab
        space === XYZ50 -> ColorSpaces.CieXyz
        space === DCI_P3 -> ColorSpaces.DciP3
        space === DisplayP3 -> ColorSpaces.DisplayP3
        space === LinearSRGB -> ColorSpaces.LinearSrgb
        space === ROMM_RGB -> ColorSpaces.ProPhotoRgb
        space == Oklab -> ColorSpaces.Oklab
        else -> null
    }

    return if (s == null) {
        val (r, g, b, a) = toSRGB().clamp()
        ComposeColor(r, g, b, a)
    }
    else {
        val (r, g, b, a) = clamp().toArray()
        ComposeColor(r, g, b, a, s)
    }
}

/**
 * Convert this color to a packed argb [color int][ColorInt].
 */
@ColorInt
public fun Color.toColorInt(): Int = toSRGB().toRGBInt().argb.toInt()

/**
 * Create an [RGB] instance from a packed argb [color int][ColorInt].
 */
public fun @receiver:ColorInt Int.toRGB(): RGB = RGBInt(toUInt()).toSRGB()

/**
 * Create an [RGBInt] instance from a packed argb [color int][ColorInt].
 */
public fun @receiver:ColorInt Int.toRGBInt(): RGBInt = RGBInt(toUInt())

/**
 * Parses a hexadecimal color string into a [Color] object.
 *
 * This function supports multiple hex color formats with optional hash prefix:
 * - **3 digits (RGB)**: Each digit is expanded to two digits (e.g., "F0A" → "FF00AA")
 * - **4 digits (ARGB)**: Each digit is expanded to two digits (e.g., "8F0A" → "88FF00AA")
 * - **6 digits (RRGGBB)**: Standard RGB format with full alpha (255)
 * - **8 digits (AARRGGBB)**: Full ARGB format with explicit alpha channel
 *
 * The hash prefix (#) is optional and will be automatically removed if present.
 * All input is converted to uppercase for consistent parsing.
 *
 * The hexadecimal color string to parse. Can include optional '#' prefix.
 * Supports formats: RGB, ARGB, RRGGBB, AARRGGBB (case-insensitive).
 *
 * @return A [ComposeColor] object with ARGB values in the range 0-255.
 *
 * @throws IllegalArgumentException
 * if the input string is not a valid hex color format or contains invalid hexadecimal characters.
 *
 */
@Suppress("MagicNumber")
public fun String.hexToColor(): ComposeColor {
    val clean = removePrefix("#").uppercase()

    return when (clean.length) {
        3 -> {
            // RGB -> RRGGBB
            val r = clean[0].digitToInt(16) * 17
            val g = clean[1].digitToInt(16) * 17
            val b = clean[2].digitToInt(16) * 17
            ComposeColor(red = r, green = g, blue = b, alpha = 255)
        }

        4 -> {
            // ARGB
            val a = clean[0].digitToInt(radix = 16) * 17
            val r = clean[1].digitToInt(16) * 17
            val g = clean[2].digitToInt(16) * 17
            val b = clean[3].digitToInt(16) * 17
            ComposeColor(alpha = a, red = r, green = g, blue = b)
        }

        6 -> {
            // RRGGBB
            val r = clean.substring(0, 2).toInt(16)
            val g = clean.substring(2, 4).toInt(16)
            val b = clean.substring(4, 6).toInt(16)
            ComposeColor(red = r, green = g, blue = b, alpha = 255)
        }

        8 -> {
            // AARRGGBB
            val a = clean.substring(0, 2).toInt(16)
            val r = clean.substring(2, 4).toInt(16)
            val g = clean.substring(4, 6).toInt(16)
            val b = clean.substring(6, 8).toInt(16)
            ComposeColor(alpha = a, red = r, green = g, blue = b)
        }

        else -> throw IllegalArgumentException("Invalid Hex color: $this")
    }
}

/**
 * Returns a new [ComposeColor] that is the inverted version of the receiver color.
 *
 * The RGB components (red, green, blue) are inverted such that each channel
 * becomes `1.0 - originalValue`. Optionally, the alpha (opacity) channel can
 * also be inverted.
 *
 * For example:
 * ```kotlin
 * val original = ComposeColor(0.2f, 0.4f, 0.6f, 1.0f)
 * val inverted = original.invert() // ComposeColor(0.8f, 0.6f, 0.4f, 1.0f)
 * val invertedAlpha = original.invert(invertAlpha = true) // ComposeColor(0.8f, 0.6f, 0.4f, 0.0f)
 * ```
 *
 * @param invertAlpha If `true`, the alpha component will also be inverted.
 *                     Defaults to `false` (alpha remains unchanged).
 * @return A new [ComposeColor] representing the inverted color.
 */
public fun ComposeColor.invert(invertAlpha: Boolean = false): ComposeColor = ComposeColor(
    1f - red,
    1f - green,
    1f - blue,
    if (invertAlpha) 1f - alpha else alpha,
)
