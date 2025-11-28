package clib.presentation.theme.shapes

import androidx.compose.foundation.shape.RoundedCornerShape as ComposeRoundedCornerShape
import androidx.compose.ui.unit.dp
import klib.data.type.cast

internal object ShapeTokens {

    val CornerExtraLarge: RoundedCornerShape = ComposeRoundedCornerShape(28.0.dp).toCornerBasedShape().cast()
    val CornerExtraLargeTop: RoundedCornerShape = ComposeRoundedCornerShape(
        topStart = 28.0.dp,
        topEnd = 28.0.dp,
        bottomEnd = 0.0.dp,
        bottomStart = 0.0.dp,
    ).toCornerBasedShape().cast()
    val CornerExtraSmall: RoundedCornerShape = ComposeRoundedCornerShape(4.0.dp).toCornerBasedShape().cast()
    val CornerExtraSmallTop: RoundedCornerShape = ComposeRoundedCornerShape(
        topStart = 4.0.dp,
        topEnd = 4.0.dp,
        bottomEnd = 0.0.dp,
        bottomStart = 0.0.dp,
    ).toCornerBasedShape().cast()
    val CornerFull = CircleShape
    val CornerLarge: RoundedCornerShape = ComposeRoundedCornerShape(16.0.dp).toCornerBasedShape().cast()
    val CornerLargeEnd: RoundedCornerShape = ComposeRoundedCornerShape(
        topStart = 0.0.dp,
        topEnd = 16.0.dp,
        bottomEnd = 16.0.dp,
        bottomStart = 0.0.dp,
    ).toCornerBasedShape().cast()
    val CornerLargeTop: RoundedCornerShape = ComposeRoundedCornerShape(
        topStart = 16.0.dp,
        topEnd = 16.0.dp,
        bottomEnd = 0.0.dp,
        bottomStart = 0.0.dp,
    ).toCornerBasedShape().cast()
    val CornerMedium: RoundedCornerShape = ComposeRoundedCornerShape(12.0.dp).toCornerBasedShape().cast()
    val CornerSmall: RoundedCornerShape = ComposeRoundedCornerShape(8.0.dp).toCornerBasedShape().cast()
}
