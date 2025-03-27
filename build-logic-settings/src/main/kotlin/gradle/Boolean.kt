package gradle

public fun Boolean.takeIfTrue(): Boolean? = takeIfTrue()

public fun Boolean.actIfTrue(action: () -> Unit) =
    if (this) act(action) else null
