package klib.data.type.primitives.color

import com.github.ajalt.colormath.model.HSL

/**
 * Extension property to check if a [HSL] is a high-light color.
 */
public val HSL.isHighLight: Boolean
    get() = l > 0.6f
