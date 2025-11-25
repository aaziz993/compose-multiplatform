package clib.presentation.color.model

public data class ColorPicker(
    val title: String = "Pick color",
    val rgbaLabel: String = "Rgba",
    val gridLabel: String = "Grid",
    val hslaLabel: String = "Hsla",
    val blendLabel: String = "Blend",
    val selectedColorTitle: String = "Hex color",
    val hexLabel: String = "Hex color",
    val close: String = "Close",
    val confirm: String = "Confirm",
)
