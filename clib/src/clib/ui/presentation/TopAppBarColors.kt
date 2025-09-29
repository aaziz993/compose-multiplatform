package clib.ui.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.serialization.Serializable

@Serializable
public data class TopAppBarColors(
    val containerColor: Color,
    val scrolledContainerColor: Color,
    val navigationIconContentColor: Color,
    val titleContentColor: Color,
    val actionIconContentColor: Color,
) {

    @OptIn(ExperimentalMaterial3Api::class)
    public fun toTopAppBarColors(): androidx.compose.material3.TopAppBarColors =
        androidx.compose.material3.TopAppBarColors(
            containerColor.toColor(),
            scrolledContainerColor.toColor(),
            navigationIconContentColor.toColor(),
            titleContentColor.toColor(),
            actionIconContentColor.toColor(),
        )
}
