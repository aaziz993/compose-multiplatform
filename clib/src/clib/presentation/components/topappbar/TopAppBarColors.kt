package clib.presentation.components.topappbar

import androidx.compose.material3.TopAppBarColors
import clib.data.type.Color
import kotlinx.serialization.Serializable

@Serializable
public data class TopAppBarColors(
    val containerColor: Color,
    val scrolledContainerColor: Color,
    val navigationIconContentColor: Color,
    val titleContentColor: Color,
    val actionIconContentColor: Color,
    val subtitleContentColor: Color,
) {

    public fun toTopAppBarColors(): TopAppBarColors =
        TopAppBarColors(
            containerColor.toColor(),
            scrolledContainerColor.toColor(),
            navigationIconContentColor.toColor(),
            titleContentColor.toColor(),
            actionIconContentColor.toColor(),
            subtitleContentColor.toColor(),
        )
}
