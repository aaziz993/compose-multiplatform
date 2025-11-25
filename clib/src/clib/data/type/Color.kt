package clib.data.type

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import com.github.ajalt.colormath.Color
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
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * Convert this color to a Colormath [Color] instance.
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
 * Convert this color to a Colormath [SRGB] instance.
 */
public fun ComposeColor.toSRGB(): RGB =
    convert(ColorSpaces.Srgb).let { SRGB(it.red, it.green, it.blue, it.alpha) }

/**
 * Convert this color to a Jetpack Compose [Color][ComposeColor] instance.
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
 * Format the integer as a hex string.
 *
 * @receiver[Int] to format.
 * @return [String] hex representation of the integer.
 */
internal fun Int.format(): String = toString(16).padStart(2, '0')

/**
 * Convert the color to a hex string.
 *
 * @receiver[Color] to convert.
 * @param[includePrefix] whether to include the '#' [prefix].
 * @return [String] hex representation of the color.
 */
public fun ComposeColor.toHex(
    includePrefix: Boolean = true,
    prefix: String = "#",
    alwaysIncludeAlpha: Boolean = false,
): String {
    val alpha = (alpha * 255).roundToInt()
    val red = (red * 255).roundToInt()
    val green = (green * 255).roundToInt()
    val blue = (blue * 255).roundToInt()

    return buildString {
        if (includePrefix) append(prefix)
        if (alwaysIncludeAlpha || alpha < 255) append(alpha.format())
        append(red.format())
        append(green.format())
        append(blue.format())
    }.uppercase()
}

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
