package klib.data.type.primitives.color

import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.ColorSpace
import com.github.ajalt.colormath.convertTo
import com.github.ajalt.colormath.model.Ansi16
import com.github.ajalt.colormath.model.Ansi256
import com.github.ajalt.colormath.model.RGB
import klib.data.type.primitives.number.denormalizeByte
import klib.data.type.primitives.number.denormalizeInt
import klib.data.type.primitives.number.normalize
import klib.data.type.primitives.number.toByteArray
import klib.data.type.primitives.number.toInt
import klib.data.type.primitives.number.unsigned
import kotlin.collections.copyOfRange
import kotlin.collections.dropLast
import kotlin.collections.fold
import kotlin.collections.indices
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.plus
import kotlin.collections.toByteArray
import kotlin.collections.toFloatArray
import kotlin.collections.toMutableList

public fun Color.toByteArray(alpha: Boolean = true): ByteArray =
    toArray().dropLast(1).let { components ->
        when (this) {
            is RGB -> components.map(Float::denormalizeByte).toByteArray()
            is Ansi16, is Ansi256 -> byteArrayOf((components[0].toInt() - Byte.MAX_VALUE - 1).toByte())

            else -> components.fold(byteArrayOf()) { acc, component ->
                acc + component.denormalizeInt().toByteArray()
            }
        } + (if (alpha) byteArrayOf(this.alpha.denormalizeByte()) else byteArrayOf())
    }

public fun <T : Color> ByteArray.toColorSpace(
    destination: ColorSpace<T>,
    alpha: Boolean = true,
    offset: Int = 0,
): T = when (destination) {
    RGB -> destination.create(
        (copyOfRange(offset, offset + 3)
            .map(Byte::normalize)
            .toMutableList() + (if (alpha) this[offset + 3].normalize() else 1f)).toFloatArray(),
    )

    Ansi16, Ansi256 -> destination.create(
        (mutableListOf(
            this[offset].unsigned().toFloat(),
        ) + (if (alpha) this[offset + 1].normalize() else 1f)).toFloatArray(),
    )

    else -> {
        val size = (destination.components.size - 1) * 4
        destination.create(
            copyOfRange(offset, offset + size)
                .let { array ->
                    ((array.indices step 4)
                        .map { array.toInt(it).normalize() }
                        .toMutableList() +
                            (if (alpha) this[offset + size].normalize() else 1f)).toFloatArray()
                },
        )
    }
}

private fun ColorSpace<*>.size(alpha: Boolean) =
    (if (alpha) 1 else 0) +
            when (this) {
                RGB, Ansi16, Ansi256 -> components.size - 1
                else -> (components.size - 1) * 4
            }

public fun <T : Color> ByteArray.toColorSpace(
    source: ColorSpace<*> = RGB,
    destination: ColorSpace<T>,
    alpha: Boolean = true,
): ByteArray = (indices step source.size(alpha)).fold(byteArrayOf()) { acc, component ->
    acc + toColorSpace(source,alpha, component).convertTo(destination).toByteArray()
}
