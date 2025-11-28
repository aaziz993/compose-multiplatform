package clib.presentation.components.topappbar

import androidx.compose.material3.TopAppBarColors
import clib.data.type.ColorSerial
import kotlinx.serialization.Serializable

@Serializable
public data class TopAppBarColors(
    val containerColor: ColorSerial,
    val scrolledContainerColor: ColorSerial,
    val navigationIconContentColor: ColorSerial,
    val titleContentColor: ColorSerial,
    val actionIconContentColor: ColorSerial,
    val subtitleContentColor: ColorSerial,
) {

    public fun toTopAppBarColors(): TopAppBarColors =
        TopAppBarColors(
            containerColor,
            scrolledContainerColor,
            navigationIconContentColor,
            titleContentColor,
            actionIconContentColor,
            subtitleContentColor,
        )
}
