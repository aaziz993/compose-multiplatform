package clib.presentation.components.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import kotlin.String

@Composable
public fun Image(
    imageSource: Any,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Unit = when (imageSource) {
    is String -> AsyncImage(
        model = imageSource,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
    )

    is ByteArray -> Image(
        imageSource.decodeToImageBitmap(),
        contentDescription,
        modifier,
        alignment,
        contentScale,
        alpha,
        colorFilter,
    )

    is Painter -> Image(
        imageSource,
        contentDescription,
        modifier,
        alignment,
        contentScale,
        alpha,
        colorFilter,
    )

    is ImageBitmap -> Image(
        imageSource,
        contentDescription,
        modifier,
        alignment,
        contentScale,
        alpha,
        colorFilter,
    )

    is ImageVector -> Image(
        imageSource,
        contentDescription,
        modifier,
        alignment,
        contentScale,
        alpha,
        colorFilter,
    )

    else -> throw IllegalArgumentException("Unknown image source type: ${imageSource::class.simpleName}")
}

