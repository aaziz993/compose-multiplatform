package klib.data.mouse.model

public sealed interface MouseEvent {

    public val x: Int?
    public val y: Int?
}

public data class MouseMove(
    override val x: Int,
    override val y: Int,
) : MouseEvent

public data class MouseWheel(
    val deltaX: Double? = null,
    val deltaY: Double? = null,
    val deltaZ: Double? = null,
    val mode: WheelMode? = null,
    override val x: Int? = null,
    override val y: Int? = null,
) : MouseEvent

public sealed interface ButtonEvent : MouseEvent {

    public val button: Button
}

public data class MouseDown(
    override val button: Button,
    override val x: Int? = null,
    override val y: Int? = null,
) : ButtonEvent

public data class MouseUp(
    override val button: Button,
    override val x: Int? = null,
    override val y: Int? = null,
) : ButtonEvent


