package clib.data.type

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

/**
 * Resize an ImageBitmap.
 * @param newWidth Resized image with.
 * @param newWidth Resized image height.
 */
public fun ImageBitmap.resize(newWidth: Int, newHeight: Int): ImageBitmap {
    val resized = ImageBitmap(newWidth, newHeight, config, hasAlpha, colorSpace)
    val canvas = Canvas(resized)
    val paint = Paint()

    canvas.drawImageRect(
        this,
        IntOffset(0, 0),
        IntSize(width, height),
        IntOffset(0, 0),
        IntSize(newWidth, newHeight),
        paint,
    )

    return resized
}


